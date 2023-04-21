package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.*;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.*;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.*;
import net.minecraft.util.MathHelper;

import java.util.*;

@ModuleInfo(getName = "Kill Aura", getCategory = Category.COMBAT)
public class KillAura extends Module {

    // i put the updateRotation in RotationUtil
    private final Setting
            // Normal
            maxCps = new Setting("Max CPS", this, 12, 1, 20, false),
            minCps = new Setting("Min CPS", this, 8, 1, 20, false),
            cpsRandom = new Setting("Random CPS", this, 2, 0, 4, false),
            reach = new Setting("Reach", this, 3, 1, 6, false),
            switchDelay = new Setting("Switch Delay", this, 250, 1, 1000, true),
            mode = new Setting("Mode", this, "Switch", "Priority"),
            sortMode = new Setting("Sort", this, "Distance", "Health"),
            pointed = new Setting("Pointing Check", this, true),
            click = new Setting("Click", this, "Normal", "Legit", "Sync"),

    // Rotations
    rotate = new Setting("Rotate", this, true),
            viewLock = new Setting("View Lock", this, false),
            movefix = new Setting("MoveFix", this, false),
            useGcd = new Setting("Use GCD", this, false),
            shakeX = new Setting("Shake X", this, 0, 0, 5, false),
            shakeY = new Setting("Shake Y", this, 0, 0, 5, false),
            minTurn = new Setting("Min Turn Speed", this, 120, 0, 180, true),
            maxTurn = new Setting("Max Turn Speed", this, 120, 0, 180, true),
    //fov = new Setting("Field of View", this, 360, 0, 360, true),

    // Auto-block
    autoBlock = new Setting("AutoBlock", this, "None", "Vanilla", "Packet", "Fake", "NCP Sync"),
           // blockRange = new Setting("Block Range", this, 8, 1, 12, false),

    // Targets
    invisibles = new Setting("Invisibles", this, false),
            players = new Setting("Players", this, false),
            animals = new Setting("Animals", this, false),
            monsters = new Setting("Monsters", this, false),
            villagers = new Setting("Villagers", this, false),
            teams = new Setting("Teams", this, false);

    private static final ArrayList<EntityLivingBase> targets = new ArrayList<>();

    public static EntityLivingBase target;
    private final TimerUtil timer = new TimerUtil(), switchTimer = new TimerUtil();
    private float[] rotations = new float[2];

    private int index;

    public KillAura() {
        add(maxCps, minCps, cpsRandom, reach, switchDelay, mode, sortMode, pointed, click, rotate, viewLock,movefix, shakeX, shakeY, useGcd, minTurn, maxTurn, /*fov,*/autoBlock, /*blockRange,*/ invisibles, players, animals, monsters, villagers, teams
        );
    }

    public void onEnable() {
        targets.clear();

        rotations[0] = mc.thePlayer.rotationYaw;
        rotations[1] = mc.thePlayer.rotationPitch;
    }

    public void onDisable() {
        target = null;
        targets.clear();

    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPost()) {
            collectTargets();
            sortTargets();
            updateIndex();

            target = !targets.isEmpty() ? targets.get(index) : null;

            if(target != null) {
                float[] rots = RotationUtil.getRotations(target, shakeX.getValue(), shakeY.getValue());

                rotations[0] = updateRotation(rotations[0], rots[0], (float) MathUtil.randomNumber(minTurn.getValue(), maxTurn.getValue()));
                rotations[1] = updateRotation(rotations[1], rots[1], (float) MathUtil.randomNumber(minTurn.getValue(), maxTurn.getValue()));
            }
        }
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                setTag(mode.getMode());

                if (target != null) {
                    doRotations((EventMotion) e, rotations);
                    doPreAutoBlock();

                    if (pointed.isEnabled() && mc.pointedEntity == null)
                        return;

                    doClick();
                }
            }
            if (e.isPost() && target != null && autoBlock.getMode().equals("Packet"))
                AuraUtil.block();
        }
        if (e instanceof EventStrafe && e.isPre() && target != null && movefix.isEnabled())
            ((EventStrafe) e).setYaw(rotations[0]);
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

    private boolean canAttack(EntityLivingBase player) {
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.isEnabled())
                return false;

            if (player instanceof EntityAnimal && !animals.isEnabled())
                return false;

            if (player instanceof EntityMob && !monsters.isEnabled())
                return false;

            if (player instanceof EntityVillager && !villagers.isEnabled())
                return false;
        }
        if (player.isOnSameTeam(mc.thePlayer) && teams.isEnabled()) return false;
        if (player.isInvisible() && !invisibles.isEnabled()) return false;
       // if (!isInFieldOfView(player, fov.getValue())) return false;
        if (isEnabled("Anti Bot") && AntiBot.getBots().contains(player)) return false;

        return player != mc.thePlayer && !(player.getDistanceToEntity(mc.thePlayer) > reach.getValue() && mc.thePlayer.isEntityAlive());
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
        if (rotate.isEnabled()) {
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
                mc.thePlayer.RotationPitchHead = event.pitch;
            }
        }
    }

    private void doClick() {
        switch (click.getMode()) {
            case "Sync":
                if (!(target.hurtTime > 8) && timer.sleep(400)) {
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
                if (timer.sleep((long) (1000 / MathUtil.randomNumber(maxCps.getValue(), minCps.getValue()) + MathUtil.randomNumber(cpsRandom.getValue(), -cpsRandom.getValue())))) {
                    if (click.getMode().equals("Normal")) {
                        mc.thePlayer.swingItem();
                        mc.playerController.attackEntity(mc.thePlayer, target);
                    } else
                        mc.clickMouse();
                }
                break;
        }
    }

    private void doPreAutoBlock() {
        if (AuraUtil.canAutoBlock(target, reach.getValue()/*blockRange.getValue()*/)) {
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

    //eumels func xd
    public static float updateRotation(float old, float needed, float amount) {
        float f = MathHelper.wrapAngleTo180_float(needed - old);

        if (f > amount) {
            f = amount;
        }

        if (f < -amount) {
            f = -amount;
        }

        return old + f;
    }
}
