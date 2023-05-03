package client.rapid.util.module;

import client.rapid.util.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class RotationUtil extends MinecraftUtil {

    public static float[] getScaffoldRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double) blockPos.getX() + 0.5 - mc.thePlayer.posX + (double) enumFacing.getFrontOffsetX() / 2.0,
        d2 = (double) blockPos.getZ() + 0.5 - mc.thePlayer.posZ + (double) enumFacing.getFrontOffsetZ() / 2.0,
        d3 = mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() - ((double) blockPos.getY() + 0.5),
        d4 = MathHelper.sqrt_double(d * d + d2 * d2);

        float f = (float) (Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f,
        f2 = (float) (Math.atan2(d3, d4) * 180.0 / Math.PI);

        if (f >= 0.0f)
            return new float[] {f, f2};

        f += 360.0f;

        return new float[] {f, f2};
    }

    // Thanks to white_cola for this
    public static float[] getRotations(final Entity entity, double shakeX, double shakeY) {
        final double xSize = entity.posX - mc.thePlayer.posX + MathUtil.randomNumber(shakeX / 50, -shakeX / 50),
        ySize = entity.posY + entity.getEyeHeight() / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + MathUtil.randomNumber(shakeY / 50, -shakeY / 50),
        zSize = entity.posZ - mc.thePlayer.posZ + MathUtil.randomNumber(shakeX / 50, -shakeX / 50),
        theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);

        final float
        yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90,
        pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));

        return new float[] {yaw, pitch};
    }

    public static float updateRotation(float old, float needed) {
        float f = MathHelper.wrapAngleTo180_float(needed - old);
        return old + f;
    }

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
