package client.rapid.module.modules.movement.jesus.jump;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventMove;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.jesus.JesusBase;
import client.rapid.util.PlayerUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

public class VulcanJesus extends JesusBase {
    private int ticks;

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onDisable() {
        ticks = 0;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            ticks++;

            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                ticks = 0;
            }
        }
        if(e instanceof EventMove && e.isPre()) {
            EventMove event = (EventMove) e;

            if((ticks > 7 || ticks < 1)) {
                event.setX(0);
                event.setZ(0);
            }
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide)e;

            if (mc.theWorld == null || mc.thePlayer.fallDistance > 3 || (mc.thePlayer.isBurning() && PlayerUtil.isOnWater()))
                return;

            if(!(event.getBlock() instanceof BlockLiquid) || mc.thePlayer.isInWater() || mc.thePlayer.isSneaking())
                return;

            event.setAxisAlignedBB(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
        }
    }

}
