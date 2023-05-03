package client.rapid.util.module;

import client.rapid.util.MinecraftUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.*;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;

public class AuraUtil extends MinecraftUtil {
    
    public static void blockPacket() {
    	if(mc.thePlayer.getHeldItem() != null)
        	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
    }

    public static void block() {
        if(mc.thePlayer.getHeldItem() != null) {
            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999999);
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
        }
    }

    public static boolean canAutoBlock(Entity target, double range) {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target.getDistanceToEntity(mc.thePlayer) < range;
    }
}
