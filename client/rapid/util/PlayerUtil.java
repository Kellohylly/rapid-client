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

}
