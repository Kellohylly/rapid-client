package client.rapid.module.modules.movement.flights;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventMove;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.Flight;
import client.rapid.util.module.MoveUtil;

public class OldNCPFlight extends FlightBase {
    private double moveSpeed;
    private boolean damaged;

    @Override
    public void onEnable() {
        if(mc.thePlayer.onGround) {
            if(damage.getMode().equals("None")) {
                mc.thePlayer.jump();
            }

            moveSpeed = speed.getValue();
        } else
            moveSpeed = MoveUtil.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        moveSpeed = moveSpeed / 5;
        MoveUtil.setMoveSpeed(moveSpeed);
    }

    @Override
    public void updateValues() {
        damaged = Flight.damaged;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if (mc.thePlayer.onGround) {
                if (!damaged && !damage.getMode().equals("None")) {
                    return;
                }

                mc.thePlayer.jump();
            }
        }
        if(e instanceof EventMove && e.isPre()) {
            EventMove event = (EventMove) e;

            mc.thePlayer.capabilities.isFlying = false;

            if(!mc.thePlayer.onGround) {
                event.setY(mc.thePlayer.ticksExisted % 2 == 0 ? -1.0E-9 : 1.0E-9);
                mc.thePlayer.motionY = 0;

                if (moveSpeed >= MoveUtil.getBaseMoveSpeed()) {
                    moveSpeed -= moveSpeed / 102;
                }

                if (mc.thePlayer.isCollidedHorizontally || !MoveUtil.isMoving()) {
                    moveSpeed = MoveUtil.getBaseMoveSpeed();
                }

                MoveUtil.setMoveSpeed(moveSpeed);
            }
        }
    }

}
