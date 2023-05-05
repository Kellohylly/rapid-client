package client.rapid.util;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.block.material.Material;

import net.minecraft.client.Minecraft;

public class PlayerUtil extends MinecraftUtil {

    public static boolean hasEffect(Potion effect) {
        return mc.thePlayer.isPotionActive(effect);
    }

    public static boolean hasBlockEquipped() {
        return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock;
    }

	public static void addChatMessage(String message) {
		message = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "R" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET + message;
		mc.thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
    public static boolean isOnWater() {
        final double y = mc.thePlayer.posY - 0.03;
        for (int x = MathHelper.floor_double(mc.thePlayer.posX); x < MathHelper.ceiling_double_int(mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(mc.thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid &&mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.water)
                    return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public static boolean isInsideBlock() {
        for(int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
            for(int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
                for(int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if(block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if(block instanceof BlockHopper)
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        if(boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }

}
