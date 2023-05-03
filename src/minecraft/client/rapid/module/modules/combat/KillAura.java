package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.*;
import client.rapid.event.events.player.EventJump;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

import java.util.*;

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

    private final Setting rotate = new Setting("Rotate", this, "None", "Instant", "Fixed");
    private final Setting viewLock = new Setting("View Lock", this, false);
    private final Setting movefix = new Setting("Move Fix", this, false);
    private final Setting rayCast = new Setting("Ray Cast", this, true);
    private final Setting useGcd = new Setting("Use GCD", this, false);
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
    private final float[] rotations = new float[2];

    private int index;

    public KillAura() {
        add(minimumCps, maximumCps, randomCps, reach, switchDelay, mode, sortMode, click, rotate, viewLock, movefix, rayCast, useGcd, shakeX, shakeY, minTurn, maxTurn, fov,autoBlock, autoblockperc, /*blockRange,*/ invisibles, players, animals, monsters, villagers, teams);
    }

    @Override
    public void onEnable() {
        rotations[0] = mc.thePlayer.rotationYaw;
        rotations[1] = mc.thePlayer.rotationPitch;
        targets.clear();
    }

    @Override
    public void onDisable() {
        target = null;
        targets.clear();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPost()) {
            collectTargets();
            sortTargets();
            updateIndex();

            target = !targets.isEmpty() ? targets.get(index) : null;

            if(target != null) {
                float[] rots = RotationUtil.getRotations(target, shakeX.getValue(), shakeY.getValue());
                rotations[0] = RotationUtil.updateRotation(rotations[0], rots[0], (float) MathUtil.randomNumber(minTurn.getValue(), maxTurn.getValue()));
                rotations[1] = RotationUtil.updateRotation(rotations[1], rots[1], (float) MathUtil.randomNumber(minTurn.getValue(), maxTurn.getValue()));
            }
        }
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                setTag(mode.getMode());

                if (target != null) {

                    if(!rotate.getMode().equals("None")) {
                        if(rotate.getMode().equals("Instant"))
                            doRotations((EventMotion) e, RotationUtil.getRotations(target, shakeX.getValue(), shakeY.getValue()));
                        else
                            doRotations((EventMotion) e, rotations);

                    }
                    doPreAutoBlock();

                    MovingObjectPosition cast = RaycastUtil.getMouseOver((float) reach.getValue());
                    if (rayCast.isEnabled() && cast != null && !cast.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY))
                        return;

                    doClick();
                }
            }
            if (e.isPost() && target != null && autoBlock.getMode().equals("Packet") && MathUtil.isinpercentage(autoblockperc.getValue()))
                AuraUtil.block();
        }
        // TODO: add for Instant rotations
        if (e instanceof EventStrafe && target != null && movefix.isEnabled())
            ((EventStrafe) e).setYaw(rotations[0]);

        if (e instanceof EventJump && target != null && movefix.isEnabled() && mc.thePlayer.onGround) {
            ((EventJump) e).setYaw(rotations[0]);
        }
    }

    private void updateIndex() {
        if (switchTimer.sleep((long) switchDelay.getValue()) && mode.getMode().equals("Switch"))
            index++;

        if (index >= targets.size())
            index = 0;
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

            if (entity.isInvisible() && !invisibles.isEnabled())
                return false;

            if (!isInFieldOfView(entity, fov.getValue()))
                return false;

            if (isEnabled("Anti Bot") && AntiBot.getBots().contains(entity))
                return false;
        }

        return entity != mc.thePlayer && !(entity.getDistanceToEntity(mc.thePlayer) > reach.getValue() && mc.thePlayer.isEntityAlive());
    }

    private boolean isInFieldOfView(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = getAngleDifference(mc.thePlayer.rotationYaw, RotationUtil.getRotations(entity, 0, 0)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }

    private float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        return f > 180F ? 360F - f : f;
    }

    private void doRotations(EventMotion event, float[] rots) {
        final float fuckVulcan = mc.gameSettings.mouseSensitivity * 0.6F,
        gcd = fuckVulcan * fuckVulcan * fuckVulcan * 1.2F;

        rots[0] = RotationUtil.updateRotation(mc.thePlayer.rotationYaw, rots[0]);
        rots[1] = RotationUtil.updateRotation(mc.thePlayer.rotationPitch, rots[1]);

        if (viewLock.isEnabled()) {
            mc.thePlayer.rotationYaw = rots[0];
            mc.thePlayer.rotationPitch = rots[1] - 12;

            if (useGcd.isEnabled()) {
                mc.thePlayer.rotationYaw -= (mc.thePlayer.rotationYaw % gcd);
                mc.thePlayer.rotationPitch -= (mc.thePlayer.rotationPitch % gcd);
            }
        } else {
            event.setYaw(rots[0]);
            event.setPitch(rots[1] - 12);

            if (useGcd.isEnabled()) {
                event.yaw -= (event.getYaw() % gcd);
                event.pitch -= (event.getPitch() % gcd);
            }
            mc.thePlayer.rotationYawHead = event.yaw;
            mc.thePlayer.renderYawOffset = event.yaw;
            mc.thePlayer.rotationPitchHead = event.pitch;
        }
    }

    private void doClick() {
        switch (click.getMode()) {
            case "Sync":
                if (!(target.hurtTime > 8) && timer.sleep(200)) {
                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, target);
                } else {
                    if(mc.thePlayer.swingProgress == 0) {
                        if(autoBlock.getMode().equals("NCP Sync")) // since ncp flags c07 now
                           PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }
                }
                break;
            case "Normal":
            case "Legit":
                if (timer.sleep((long) (1000 / MathUtil.randomNumber(minimumCps.getValue(), maximumCps.getValue()) + MathUtil.randomNumber(randomCps.getValue(), -randomCps.getValue())))) {
                    if (click.getMode().equals("Normal")) {
                        mc.thePlayer.swingItem();
                        mc.playerController.attackEntity(mc.thePlayer, target);
                    } else {
                        mc.thePlayer.swingItem();

                        if(mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null)
                            mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                    }
                        //mc.clickMouse();
                }
                break;
        }
    }

    private void doPreAutoBlock() {
        if (AuraUtil.canAutoBlock(target, reach.getValue()/*blockRange.getValue()*/) && MathUtil.isinpercentage(autoblockperc.getValue())) {
            switch (autoBlock.getMode()) {
                case "Vanilla":
                    AuraUtil.block();
                    break;
                case "Packet":
                    AuraUtil.blockPacket();
                    break;
                case "NCP Sync": // thanks Kellohylly for giving me an idea using C09.
                    PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
                    PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    break;
            }
        }
    }
}
