package client.rapid.module.modules.movement.speeds.verus;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.module.MoveUtil;

public class VerusGroundSpeed extends SpeedBase {
    private int ticks = 0;
    private boolean canSpeed;
    private boolean jumped;

    @Override
    public void onEnable() {
        if(mc.thePlayer.onGround) {
            ticks = 9;
            canSpeed = true;
        }
    }

    @Override
    public void onDisable() {
        canSpeed = false;
        ticks = 0;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            boolean sprinting = mc.thePlayer.isSprinting();
            boolean jumpKeyDown = mc.gameSettings.keyBindJump.isKeyDown();

            mc.thePlayer.capabilities.isFlying = false;

            if(Wrapper.getModuleManager().getModule("Flight").isEnabled()) {
                return;
            }

            if(mc.thePlayer.onGround && !jumpKeyDown) {
                ticks++;

                if(ticks >= 10) {
                    mc.thePlayer.jump();
                    ticks = 0;
                    canSpeed = true;
                    jumped = true;
                }
                if(canSpeed) {
                    if(sprinting) {
                        if (mc.thePlayer.moveForward > 0) {
                            MoveUtil.setMoveSpeed(0.45);
                        } else {
                            MoveUtil.setMoveSpeed(0.42);
                        }
                    } else {
                        MoveUtil.setMoveSpeed(0.3);
                    }
                }

                if(canSpeed && !MoveUtil.isMoving()) {
                    canSpeed = false;
                }

            } else {
                if(jumped && !jumpKeyDown) {
                    if(MoveUtil.isMoving()) {
                        mc.thePlayer.motionY = -0.0980000019;
                    }
                    jumped = false;
                }

                MoveUtil.setMoveSpeed(sprinting ? 0.35 : 0.3);
            }
        }
    }

}
