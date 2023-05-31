package client.rapid.module.modules.movement.jesus.jump;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.jesus.JesusBase;
import client.rapid.util.PlayerUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

public class VerusJesus extends JesusBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide)e;

            if (mc.theWorld == null || mc.thePlayer.fallDistance > 3 || (mc.thePlayer.isBurning() && PlayerUtil.isOnWater()))
                return;

            if(!(event.getBlock() instanceof BlockLiquid) || mc.thePlayer.isInWater() || mc.thePlayer.isSneaking())
                return;

            event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
        }
    }

}
