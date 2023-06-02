package client.rapid.module.modules.movement.longjumps;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.util.module.MoveUtil;

public class VanillaLongJump extends LongJumpBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY *= height.getValue();
            } else {
                MoveUtil.setMoveSpeed(speed.getValue());
            }
        }
    }

}
