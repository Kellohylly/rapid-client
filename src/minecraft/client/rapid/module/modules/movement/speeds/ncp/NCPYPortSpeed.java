package client.rapid.module.modules.movement.speeds.ncp;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.module.MoveUtil;

public class NCPYPortSpeed extends SpeedBase {

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            if (MoveUtil.isMoving()) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    MoveUtil.setMoveSpeed(0.35);

                } else {
                    mc.thePlayer.motionY = -0.5;
                }
            }
            MoveUtil.setMoveSpeed((MoveUtil.getMoveSpeed()) + (mc.thePlayer.hurtTime > 0 ? damageBoost.getValue() / 5 : 0));
            mc.timer.timerSpeed = 1.08f;
        }
    }

}
