package client.rapid.util.module;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import client.rapid.util.MathUtil;
import client.rapid.util.MinecraftUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil extends MinecraftUtil {

    public static float prevYaw, prevPitch, yaw, pitch;
	
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

    public static Vec3 resolveOptimizedHitBox(Vec3 look, AxisAlignedBB axisAlignedBB) {
        return new Vec3(MathHelper.clamp_double(look.xCoord, axisAlignedBB.minX, axisAlignedBB.maxX), MathHelper.clamp_double(look.yCoord, axisAlignedBB.minY, axisAlignedBB.maxY), MathHelper.clamp_double(look.zCoord, axisAlignedBB.minZ, axisAlignedBB.maxZ));
    }
    
    // Thanks to white_cola for this
    public static float[] getRotations(final Entity entity, double shakeX, double shakeY, boolean dontShake, boolean heuristics, boolean prediction, boolean resolver) {
    	Vec3 targetPosition = new Vec3(entity.posX, entity.posY + entity.getEyeHeight() / 2, entity.posZ);
    	if(resolver) {
    		targetPosition = resolveOptimizedHitBox(mc.thePlayer.getPositionEyes(1F), entity.getEntityBoundingBox());
    	}
        double xSize = targetPosition.xCoord - mc.thePlayer.posX + (dontShake ? 0 : MathUtil.randomNumber(shakeX / 50, -shakeX / 50)),
        ySize = targetPosition.yCoord - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + (dontShake ? 0 : MathUtil.randomNumber(shakeY / 50, -shakeY / 50)),
        zSize = targetPosition.zCoord - mc.thePlayer.posZ + (dontShake ? 0 : MathUtil.randomNumber(shakeX / 50, -shakeX / 50));

        if (prediction) {
            final boolean sprinting = entity.isSprinting();
            final boolean sprintingPlayer = mc.thePlayer.isSprinting();

            final float walkingSpeed = 0.10000000149011612f; //https://minecraft.fandom.com/wiki/Sprinting

            final float sprint = sprinting ? 1.25f : walkingSpeed;
            final float playerSprint = sprintingPlayer ? 1.25f : walkingSpeed;

            final float predictX = (float) ((entity.posX - entity.prevPosX) * sprint);
            final float predictZ = (float) ((entity.posZ - entity.prevPosZ) * sprint);

            final float playerPredictX = (float) ((mc.thePlayer.posX - mc.thePlayer.prevPosX) * playerSprint);
            final float playerPredictZ = (float) ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * playerSprint);


            if (predictX != 0.0f && predictZ != 0.0f || playerPredictX != 0.0f && playerPredictZ != 0.0f) {
                xSize += predictX + playerPredictX;
                zSize += predictZ + playerPredictZ;
            }
        }
        
        if(heuristics) {
        	try {
                xSize += SecureRandom.getInstanceStrong().nextDouble() * 0.2;
                ySize += SecureRandom.getInstanceStrong().nextDouble() * 0.5;
                zSize += SecureRandom.getInstanceStrong().nextDouble() * 0.2;	
			} catch (NoSuchAlgorithmException e) {
				System.out.println("what");
				e.printStackTrace();
			}
        }
        
        double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90,
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
