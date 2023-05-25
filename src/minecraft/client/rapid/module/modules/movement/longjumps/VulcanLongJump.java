package client.rapid.module.modules.movement.longjumps;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.MoveUtil;

public class VulcanLongJump extends LongJumpBase {
    private int jumps;

    private final TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        jumps = 0;
        timer.reset();
    }

    @Override
    public void onDisable() {
        jumps = 0;
        timer.reset();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();

                // High jump
            } else if(mc.thePlayer.fallDistance > 0) {
                if(jumps < 3) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (jumps == 0 ? 10 : height.getValue()), mc.thePlayer.posZ);
                    jumps += 1;
                } else {

                    // Glide
                    if(timer.sleep(146)) {
                        mc.thePlayer.motionY = -0.1476;
                    } else {
                        mc.thePlayer.motionY = -0.0975;
                    }

                    // Speed boost / No fall damage
                    if(mc.thePlayer.ticksExisted % 11 == 0) {
                        if(mc.thePlayer.ticksExisted % 5.5 == 0) {
                            mc.thePlayer.onGround = true;
                        }

                        if(mc.gameSettings.keyBindForward.isKeyDown()) {
                            MoveUtil.setMoveSpeed(0.48);
                        }
                    }

                }
            }
        }
    }

}
