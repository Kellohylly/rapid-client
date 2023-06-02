package client.rapid.module.modules.movement.speeds.vulcan;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.potion.Potion;

public class VulcanLowHopSpeed extends SpeedBase {
    private int ticks;

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onDisable() {
        ticks = 0;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            ticks++;
            
            if (MoveUtil.isMovingOnGround()) {
                double moveSpeed;

                mc.thePlayer.jump();
                mc.timer.timerSpeed = 1.15f;
                if (mc.thePlayer.moveForward != 0 && mc.thePlayer.moveStrafing == 0)
                    moveSpeed = 0.522;
                else if (mc.thePlayer.moveStrafing != 0)
                    moveSpeed = 0.46;
                else
                    moveSpeed = MoveUtil.getBaseMoveSpeed() * 1.8;

                if (PlayerUtil.hasEffect(Potion.moveSpeed))
                    moveSpeed *= 1.0 + 0.09 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

                if (PlayerUtil.hasEffect(Potion.moveSlowdown))
                    moveSpeed *= 0.9f;

                MoveUtil.setMoveSpeed(moveSpeed);
                ticks = 0;
            } else {
                mc.timer.timerSpeed = 1f;

                if (MoveUtil.getMoveSpeed() > 0.332)
                    MoveUtil.setMoveSpeed(0.331);

                if (ticks == 4)
                    mc.thePlayer.motionY = -0.315;

                if (ticks > 3)
                    mc.thePlayer.motionY -= 0.0004;

                if (!groundStrafe.isEnabled())
                    MoveUtil.strafe();
            }
        }
    }

}
