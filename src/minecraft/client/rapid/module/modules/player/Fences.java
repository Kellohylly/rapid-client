package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventSafewalk;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;

@ModuleInfo(getName = "Fences", getCategory = Category.PLAYER)
public class Fences extends Module {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSafewalk && e.isPre()) {
            BlockPos under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
            BlockPos under2 = new BlockPos(mc.thePlayer.posX, under.getY() - 1, mc.thePlayer.posZ);
            BlockPos under3 = new BlockPos(mc.thePlayer.posX, under.getY() - 1, mc.thePlayer.posZ);

            if(mc.theWorld.isAirBlock(under) && mc.theWorld.isAirBlock(under2) && (mc.theWorld.isAirBlock(under2) || mc.theWorld.getBlockState(under3).getBlock() instanceof BlockLiquid)) {
                e.cancel();
            }
        }
    }
}
