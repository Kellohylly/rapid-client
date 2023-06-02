package client.rapid.module.modules.movement.steps.motion;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.steps.StepBase;
import client.rapid.util.module.MoveUtil;

public class NCPStep extends StepBase {
    private boolean stepped;

    @Override
    public void onEnable() {
        stepped = false;
    }

    @Override
    public void onDisable() {
        stepped = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if (mc.thePlayer.isCollidedHorizontally && MoveUtil.isMovingOnGround()) {
                mc.thePlayer.jump();
                stepped = true;
            } else {
                if (!mc.thePlayer.isCollidedHorizontally && stepped && MoveUtil.isMoving()) {
                    mc.thePlayer.motionY = 0;
                    timer.reset();
                    stepped = false;

                    MoveUtil.setMoveSpeed(0.2);
                }
            }
        }
    }

}
