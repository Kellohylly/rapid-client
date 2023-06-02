package client.rapid.module.modules.movement.longjumps.ncp;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.longjumps.LongJumpBase;
import client.rapid.util.module.MoveUtil;

public class NCPDevLongJump extends LongJumpBase {
    private double moveSpeed;
    private int ticks;
    private boolean jumped;

    @Override
    public void onDisable() {
        ticks = 0;
        jumped = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            ticks++;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY *= 1.04;
                moveSpeed = 0.48;
                jumped = true;
                ticks = 0;
            } else {
                if(ticks == 1) {
                    moveSpeed = 0.44;
                } else {
                    if(mc.thePlayer.fallDistance == 0)
                        moveSpeed -= moveSpeed / 26;
                }
            }

            MoveUtil.setMoveSpeed(moveSpeed);
        }
    }
}
