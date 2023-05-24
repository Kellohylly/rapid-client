package client.rapid.module.modules.movement.flights.vanilla;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.flights.FlightBase;
import client.rapid.util.module.MoveUtil;

public class MotionFlight extends FlightBase {
    private double moveSpeed;

    @Override
    public void onDisable() {
        moveSpeed = moveSpeed / 5;
        MoveUtil.setMoveSpeed(moveSpeed);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            mc.thePlayer.capabilities.isFlying = false;

            moveSpeed = speed.getValue();

            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.thePlayer.motionY = moveSpeed / 2;

            else if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.thePlayer.motionY = -moveSpeed / 2;
            else
                mc.thePlayer.motionY = 0;

            MoveUtil.setMoveSpeed(moveSpeed);
        }
    }

}
