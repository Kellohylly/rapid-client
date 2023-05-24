package client.rapid.module.modules.movement.speeds.verus;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.module.MoveUtil;

public class VerusGroundSpeed extends SpeedBase {

    @Override
    public void onEnable() {
        if(mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            if (mc.thePlayer.fallDistance <= 0) {
                if (mc.thePlayer.onGround) {
                    if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.ticksExisted % 13 == 0) {
                        mc.thePlayer.jump();
                        MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() + 0.2125);
                    } else
                        MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() + 0.1125);
                } else {
                    if (!mc.gameSettings.keyBindJump.isKeyDown() && MoveUtil.isMoving()) {
                        mc.thePlayer.motionY = -0.0980000019;
                        MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() + 0.08);
                    }
                }
            }
            MoveUtil.setMoveSpeed(MoveUtil.getMoveSpeed() + (mc.thePlayer.hurtTime != 0 ? damageBoost.getValue() : 0));
        }
    }

}
