package client.rapid.util.module;

import client.rapid.util.MinecraftUtil;
import net.minecraft.potion.Potion;

public class MoveUtil extends MinecraftUtil {

    public static void strafe() {
        setMoveSpeed(getMoveSpeed());
    }

    public static void setMoveSpeed(double moveSpeed) {
        setMoveSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static float getMoveSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static boolean isMoving() {
        return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
    }

    public static boolean isMovingOnGround() {
        return isMoving() && mc.thePlayer.onGround;
    }

    public static void setMoveSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            if (strafe > 0.0D)
                yaw += (forward > 0.0D) ? -45 : 45;

            else if (strafe < 0.0D)
                yaw += (forward > 0.0D) ? 45 : -45;

            strafe = 0.0D;

            if (forward > 0.0D)
                forward = 1.0D;

            else if (forward < 0.0D)
                forward = -1.0D;
        }
        if (strafe > 0.0D)
            strafe = 1.0D;

        else if (strafe < 0.0D)
            strafe = -1.0D;

        double
                mx = Math.cos(Math.toRadians(yaw + 90.0F)),
                mz = Math.sin(Math.toRadians(yaw + 90.0F));

        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static double getBaseMoveSpeed() {
        double base = 0.2875;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            base *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

        return base;
    }
}
