package client.rapid.module.modules.movement.speeds.vulcan;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.potion.Potion;

public class VulcanHopSpeed extends SpeedBase {
    private double moveSpeed;

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if (MoveUtil.isMovingOnGround()) {
                mc.thePlayer.jump();

                if (mc.thePlayer.moveForward != 0 && mc.thePlayer.moveStrafing == 0) {
                    moveSpeed = 0.522;
                } else if (mc.thePlayer.moveStrafing != 0) {
                    moveSpeed = 0.46;
                } else {
                    moveSpeed = MoveUtil.getBaseMoveSpeed() * 1.8;
                }

                if (PlayerUtil.hasEffect(Potion.moveSpeed)) {
                    moveSpeed *= 1.0 + 0.09 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
                }

                if (PlayerUtil.hasEffect(Potion.moveSlowdown)) {
                    moveSpeed *= 0.9f;
                }

                MoveUtil.setMoveSpeed(moveSpeed);
            } else {

                if (MoveUtil.getMoveSpeed() > 0.332) {
                    MoveUtil.setMoveSpeed(0.331);
                }

                if (!groundStrafe.isEnabled()) {
                    MoveUtil.strafe();
                }
            }
        }
    }

}
