package client.rapid.module.modules.movement.speeds.ncp;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.potion.Potion;

public class NCPHopSpeed extends SpeedBase {
    private double moveSpeed;
    private int ticks;

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            ticks++;

            if (MoveUtil.isMovingOnGround()) {
                mc.thePlayer.jump();
                ticks = 0;

                if (mc.thePlayer.moveForward != 0) {
                    if (mc.thePlayer.moveStrafing != 0)
                        moveSpeed = 0.45;
                    else
                        moveSpeed = 0.48;
                } else {
                    moveSpeed = 0.4f;
                }
                if (PlayerUtil.hasEffect(Potion.moveSpeed))
                    moveSpeed *= 1.0 + 0.17 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

                if (PlayerUtil.hasEffect(Potion.moveSlowdown))
                    moveSpeed *= 0.9f;

                MoveUtil.setMoveSpeed(moveSpeed);
                mc.thePlayer.motionY -= 0.01f;
                mc.timer.timerSpeed = 1.13f;
                mc.thePlayer.speedInAir = 0.021f;
            } else {
                MoveUtil.setMoveSpeed(MoveUtil.getMoveSpeed() * 1.0004);

                if (ticks == 5) {
                    mc.timer.timerSpeed = 1.01f;
                }
                if (mc.thePlayer.hurtTime != 0)
                    MoveUtil.setMoveSpeed(MoveUtil.getMoveSpeed() + damageBoost.getValue() / 5);
            }
        }
    }

}
