package client.rapid.module.modules.movement.longjumps.ncp;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.longjumps.LongJumpBase;
import client.rapid.util.module.MoveUtil;

public class OldNCPLongJump extends LongJumpBase {
    private double moveSpeed;

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();

                moveSpeed = speed.getValue();
            } else {
                if (moveSpeed > MoveUtil.getBaseMoveSpeed()) {
                    moveSpeed -= moveSpeed / 109;
                }

                MoveUtil.setMoveSpeed(moveSpeed);
            }
        }
    }

}
