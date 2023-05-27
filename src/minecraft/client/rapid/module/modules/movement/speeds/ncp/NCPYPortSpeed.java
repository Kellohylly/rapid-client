package client.rapid.module.modules.movement.speeds.ncp;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.module.MoveUtil;
import net.minecraft.util.BlockPos;

public class NCPYPortSpeed extends SpeedBase {

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            boolean nearEnd = mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ));

            if (MoveUtil.isMoving()) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();

                    if(nearEnd) {
                        MoveUtil.setMoveSpeed(0.48);
                    } else {
                        MoveUtil.setMoveSpeed(0.351);
                        mc.thePlayer.motionY = 0.399;
                    }
                } else {
                    if(!nearEnd) {
                        mc.thePlayer.motionY = -0.5;
                    }
                }
            }
            mc.timer.timerSpeed = 1.08f;

            MoveUtil.setMoveSpeed((MoveUtil.getMoveSpeed()) + (mc.thePlayer.hurtTime > 0 ? damageBoost.getValue() / 5 : 0));
        }
    }

}
