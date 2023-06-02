package client.rapid.module.modules.movement.speeds.verus;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.potion.Potion;

public class VerusHopSpeed extends SpeedBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            double moveSpeed = mc.gameSettings.keyBindBack.isKeyDown() || MoveUtil.isMoving() && mc.thePlayer.moveForward == 0 ? 0.32 : (mc.thePlayer.isSprinting() ? 0.37 : 0.34);

            if (mc.thePlayer.isSneaking())
                return;

            if (MoveUtil.isMovingOnGround()) {
                mc.thePlayer.jump();
                moveSpeed = mc.gameSettings.keyBindBack.isKeyDown() || MoveUtil.isMoving() && mc.thePlayer.moveForward == 0 ? 0.4 : (mc.thePlayer.isSprinting() || mc.thePlayer.isSneaking() ? 0.54 : 0.51);
            }

            if (PlayerUtil.hasEffect(Potion.moveSpeed))
                moveSpeed *= 1.0 + 0.17 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

            if (PlayerUtil.hasEffect(Potion.moveSlowdown))
                moveSpeed *= 0.9f;

            if (damageBoost.getValue() > 0 && mc.thePlayer.hurtTime > 0)
                moveSpeed = moveSpeed + damageBoost.getValue();

            MoveUtil.setMoveSpeed(moveSpeed);
        }
    }

}
