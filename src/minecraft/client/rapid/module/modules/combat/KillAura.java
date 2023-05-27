package client.rapid.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventRotation;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.MathUtil;
import client.rapid.util.PacketUtil;
import client.rapid.util.RaycastUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.AuraUtil;
import client.rapid.util.module.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(getName = "Kill Aura", getCategory = Category.COMBAT)
public class KillAura extends Module {
    private final Setting minimumCps = new Setting("Max CPS", this, 12, 1, 20, false);
    private final Setting maximumCps = new Setting("Min CPS", this, 8, 1, 20, false);
    private final Setting randomCps = new Setting("Random CPS", this, 2, 0, 4, false);
    private final Setting reach = new Setting("Reach", this, 3, 3, 6, false);
    private final Setting switchDelay = new Setting("Switch Delay", this, 250, 1, 1000, true);
    private final Setting mode = new Setting("Mode", this, "Switch", "Priority");
    private final Setting sortMode = new Setting("Sort", this, "Distance", "Health");
    private final Setting click = new Setting("Click", this, "Normal", "Legit", "Sync");

    private final Setting rotate = new Setting("Rotate", this, "None", "Normal");
    private final Setting viewLock = new Setting("View Lock", this, false);
    private final Setting rayCast = new Setting("Ray Cast", this, true);
    private final Setting useGcd = new Setting("Use GCD", this, false);
    private final Setting heuristics = new Setting("Heuristics", this, false);
    private final Setting prediction = new Setting("Prediction", this, false);
    private final Setting resolver = new Setting("Resolver", this, false);
    private final Setting legitRandom = new Setting("Legit Random", this, false);
    private final Setting shakeX = new Setting("Random X", this, 0, 0, 5, false);
    private final Setting shakeY = new Setting("Random Y", this, 0, 0, 5, false);
    private final Setting minTurn = new Setting("Min Turn Speed", this, 120, 0, 180, true);
    private final Setting maxTurn = new Setting("Max Turn Speed", this, 120, 0, 180, true);
    private final Setting fov = new Setting("Field of View", this, 360, 0, 360, true);

    private final Setting autoBlock = new Setting("AutoBlock", this, "None", "Vanilla", "Packet", "Fake", "NCP");
    private final Setting autoblockperc = new Setting("Autoblock %", this, 0, 0, 100, true);
    //blockRange = new Setting("Block Range", this, 8, 1, 12, false),

    private final Setting invisibles = new Setting("Invisibles", this, false);
    private final Setting players = new Setting("Players", this, false);
    private final Setting animals = new Setting("Animals", this, false);
    private final Setting monsters = new Setting("Monsters", this, false);
    private final Setting villagers = new Setting("Villagers", this, false);
    private final Setting teams = new Setting("Teams", this, false);

    private static final ArrayList<EntityLivingBase> targets = new ArrayList<>();

    public static EntityLivingBase target;
    private final TimerUtil timer = new TimerUtil(), switchTimer = new TimerUtil();

    private int index;

    public KillAura() {
        add(minimumCps, maximumCps, randomCps, reach, switchDelay, mode, sortMode, click, rotate, viewLock, rayCast, useGcd, heuristics, prediction, resolver, legitRandom, shakeX, shakeY, minTurn, maxTurn, fov,autoBlock, autoblockperc, /*blockRange,*/ invisibles, players, animals, monsters, villagers, teams);
    }

    @Override
    public void settingCheck() {
        viewLock.setVisible(!rotate.getMode().equals("None"));
        autoblockperc.setVisible(!autoBlock.getMode().equals("None"));
        useGcd.setVisible(!rotate.getMode().equals("None"));
        shakeX.setVisible(!rotate.getMode().equals("None"));
        shakeY.setVisible(!rotate.getMode().equals("None"));
        minTurn.setVisible(!rotate.getMode().equals("None"));
        maxTurn.setVisible(!rotate.getMode().equals("None"));
        switchDelay.setVisible(mode.getMode().equals("Switch"));

    }

    @Override
    public void onEnable() {
        targets.clear();
    }

    @Override
    public void onDisable() {
        target = null;
        targets.clear();
    }

    private TimerUtil legitShakeTimer = new TimerUtil();
    private boolean up = false;
    private double legitShakeDelay = 0;
    
    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPost()) {
            collectTargets();
            sortTargets();

            if (switchTimer.sleep((long) switchDelay.getValue()) && mode.getMode().equals("Switch"))
                index++;

            if (index >= targets.size())
				index = 0;

			target = !targets.isEmpty() ? targets.get(index) : null;
		}
		if (e instanceof EventRotation) {
			EventRotation event = (EventRotation) e;
			if (target != null) {
				if (!rotate.getMode().equals("None")) {
					float[] rotations = RotationUtil.getRotations(target, shakeX.getValue(), shakeY.getValue(),
							legitRandom.isEnabled(), heuristics.isEnabled(), prediction.isEnabled(),
							resolver.isCheck());
					if (this.legitRandom.isEnabled()) {
						if (up) {
							rotations[1] += ThreadLocalRandom.current().nextDouble(shakeY.getValue() * 0.5,
									shakeY.getValue() * 2);
						} else {
							rotations[1] -= ThreadLocalRandom.current().nextDouble(shakeY.getValue() * 0.5,
									shakeY.getValue() * 2);
						}

						if (legitShakeTimer.reached(legitShakeDelay)) {
							legitShakeTimer.reset();
							up = !up;
							legitShakeDelay = ThreadLocalRandom.current().nextDouble(100, 600);
						}
					}
					doRotations(event, rotations);
				}
			}
		}
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                setTag(mode.getMode());

                if (target != null) {
                    doPreAutoBlock();

                    MovingObjectPosition cast = RaycastUtil.getMouseOver((float) reach.getValue());

                    if (rayCast.isEnabled() && cast != null && !cast.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY))
                        return;

                    doClick();
                }
            }
            if (e.isPost() && target != null && MathUtil.isInPercentage(autoblockperc.getValue())) {
                if(autoBlock.getMode().equals("Packet"))
                    AuraUtil.block();

                else if(autoBlock.getMode().equals("NCP"))
                    PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            }
        }

        // Auto Disable
        autoDisable(e);
    }

    private void collectTargets() {
        targets.clear();

        mc.thePlayer.worldObj.loadedEntityList.stream().filter(e -> e instanceof EntityLivingBase && canAttack((EntityLivingBase) e))
        .forEach(e -> targets.add((EntityLivingBase) e));
    }

    private void sortTargets() {
        switch (sortMode.getMode()) {
        case "Distance":
            targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
            break;
        case "Health":
            targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
            break;
        }
    }

    private boolean canAttack(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer || entity instanceof EntityAnimal || entity instanceof EntityMob || entity instanceof EntityVillager) {
            if (entity instanceof EntityPlayer && !players.isEnabled())
                return false;

            if (entity instanceof EntityAnimal && !animals.isEnabled())
                return false;

            if (entity instanceof EntityMob && !monsters.isEnabled())
                return false;

            if (entity instanceof EntityVillager && !villagers.isEnabled())
                return false;

            if (entity.isOnSameTeam(mc.thePlayer) && teams.isEnabled())
                return false;

        }
        if (entity.isInvisible() && !invisibles.isEnabled())
            return false;

        if (!isInFieldOfView(entity, fov.getValue()))
            return false;

        if (isEnabled("Anti Bot") && AntiBot.getBots().contains(entity))
            return false;

        if (entity.isOnSameTeam(mc.thePlayer) && teams.isEnabled())
            return false;

        if(!entity.isEntityAlive())
            return false;

        return entity != mc.thePlayer && !(entity.getDistanceToEntity(mc.thePlayer) > reach.getValue());
    }

    private boolean isInFieldOfView(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = getAngleDifference(mc.thePlayer.rotationYaw, RotationUtil.getRotations(entity, 0, 0, false, false, false, false)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }

    private float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        return f > 180F ? 360F - f : f;
    }

    public float[] applyMouseFix(float newYaw, float newPitch) {
        final float curYaw = RotationUtil.yaw;
        final float curPitch = RotationUtil.pitch;
        
        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 1.2F;
        
        final float deltaYaw = newYaw - curYaw;
        final float deltaPitch = newPitch - curPitch;
        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);
        
        final float fixedYaw = curYaw + fixedDeltaYaw;
        final float fixedPitch = curPitch + fixedDeltaPitch;
        return new float[] {fixedYaw, fixedPitch};
    }
    
    public float[] smoothenRotations(float[] rots) {
        final float curYaw = RotationUtil.yaw;
        final float curPitch = RotationUtil.pitch;
        
        final int fps = (int) (Minecraft.getDebugFPS() / 20.0F);
        final float rotationStep = (float) MathUtil.randomNumber(this.maxTurn.getValue(), this.minTurn.getValue());
        
        final float advancedDeltaYaw = (((rots[0] - curYaw) + 540) % 360) - 180;
        final float advancedDeltaPitch = rots[1] - curPitch;

        final float advancedDistanceYaw = MathHelper.clamp_float(advancedDeltaYaw, -rotationStep, rotationStep) / fps * 4;
        final float advancedDistancePitch = MathHelper.clamp_float(advancedDeltaPitch, -rotationStep, rotationStep) / fps * 4;

        return new float[] {curYaw + advancedDistanceYaw, curPitch + advancedDistancePitch};
    }
    
    private void doRotations(EventRotation event, float[] rots) {
        float f = (float)(mc.thePlayer.posX - target.posX);
        float f2 = (float)(mc.thePlayer.posZ - target.posZ);
        double distance = MathHelper.sqrt_float(f * f + f2 * f2);

        /*if(distance < 0.42) {
            rots[0] = RotationUtil.updateRotation(RotationUtil.yaw, 0);

        } else {*/
            rots[0] = RotationUtil.updateRotation(RotationUtil.yaw, rots[0]);
        //}

        rots[1] = RotationUtil.updateRotation(RotationUtil.pitch, rots[1]);

        rots = this.smoothenRotations(rots);
        
    	if(this.useGcd.isEnabled()) {
    		rots = this.applyMouseFix(rots[0], rots[1]);
    	}
        
        event.setYaw(rots[0]);
        event.setPitch(rots[1]);
    	
        if (viewLock.isEnabled()) {
            mc.thePlayer.rotationYaw = rots[0];
            mc.thePlayer.rotationPitch = rots[1];
        } else {
            mc.thePlayer.rotationYawHead = event.getYaw();
            mc.thePlayer.renderYawOffset = event.getYaw();
            mc.thePlayer.rotationPitchHead = event.getPitch();
        }
    }

    private void doClick() {
        switch (click.getMode()) {
            case "Sync":
                if (!(target.hurtTime > 8) && timer.sleep(200)) {
                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, target);
                }
                break;
            case "Normal":
            case "Legit":
                if (timer.sleep((long) (1000 / MathUtil.randomNumber(minimumCps.getValue(), maximumCps.getValue()) + MathUtil.randomNumber(randomCps.getValue(), -randomCps.getValue())))) {
                    if (click.getMode().equals("Normal")) {
                        mc.thePlayer.swingItem();
                        mc.playerController.attackEntity(mc.thePlayer, target);
                    } else {
                    	mc.clickMouse();
                    }
                }
                break;
        }
    }

    private void doPreAutoBlock() {
        if (AuraUtil.canAutoBlock(target, reach.getValue()/*blockRange.getValue()*/) && MathUtil.isInPercentage(autoblockperc.getValue())) {
            switch (autoBlock.getMode()) {
                case "Vanilla":
                    AuraUtil.block();
                    break;
                case "Packet":
                    AuraUtil.blockPacket();
                    break;
            }
        }
    }
}
