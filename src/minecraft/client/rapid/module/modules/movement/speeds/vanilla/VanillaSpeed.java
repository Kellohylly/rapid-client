package client.rapid.module.modules.movement.speeds.vanilla;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.module.MoveUtil;

public class VanillaSpeed extends SpeedBase {

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            if (MoveUtil.isMovingOnGround())
                mc.thePlayer.jump();

            MoveUtil.setMoveSpeed(speed.getValue() / 10);
        }
    }
}
