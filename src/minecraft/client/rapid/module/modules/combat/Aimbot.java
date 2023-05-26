package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventRotation;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.RotationUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

@ModuleInfo(getName = "Aimbot", getCategory = Category.COMBAT)
public class Aimbot extends Module {
    private final Setting reach = new Setting("Reach", this, 3, 3, 6, false);
    private final Setting turnSpeed = new Setting("Turn Speed", this, 30, 0, 180, true);
    private final Setting teams = new Setting("Teams", this, false);

    private static final ArrayList<EntityLivingBase> targets = new ArrayList<>();

    public static EntityLivingBase target;
    private final float[] rotations = new float[2];

    public Aimbot() {
        add(reach, turnSpeed, teams);
    }

    @Override
    public void onEnable() {
        rotations[0] = mc.thePlayer.rotationYaw;
        rotations[1] = mc.thePlayer.rotationPitch;
        targets.clear();
    }

    @Override
    public void onDisable() {
        targets.clear();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRotation) {
            if(target != null) {
                float[] rots = RotationUtil.getRotations(target, 0, 0, false, false, false);

                rotations[0] = RotationUtil.updateRotation(mc.thePlayer.rotationYaw, rots[0], (float)turnSpeed.getValue());
                rotations[1] = RotationUtil.updateRotation(mc.thePlayer.rotationPitch, rots[1], (float)turnSpeed.getValue());

                mc.thePlayer.rotationYaw = rotations[0];
                mc.thePlayer.rotationPitch = rotations[1];
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            collectTargets();

            target = !targets.isEmpty() ? targets.get(0) : null;
        }
    }

    private void collectTargets() {
        targets.clear();

        mc.thePlayer.worldObj.loadedEntityList.stream().filter(e -> e instanceof EntityLivingBase && canAttack((EntityLivingBase) e)).forEach(e -> targets.add((EntityLivingBase) e));
    }

    private boolean canAttack(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            if (entity.isOnSameTeam(mc.thePlayer) && teams.isEnabled())
                return false;

            if (isEnabled("Anti Bot") && AntiBot.getBots().contains(entity))
                return false;
        }

        return entity != mc.thePlayer && !(entity.getDistanceToEntity(mc.thePlayer) >= reach.getValue() && mc.thePlayer.isEntityAlive());
    }

}
