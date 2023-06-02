package client.rapid.module.modules.movement.jesus.other;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.jesus.JesusBase;

public class MatrixJesus extends JesusBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.isInWater()) {
                if(mc.thePlayer.isCollidedHorizontally) {
                    mc.thePlayer.motionY = 0.22;
                } else {
                    mc.thePlayer.motionY = 0.13;
                }
            }
            mc.gameSettings.keyBindJump.pressed = false;
        }
    }

}
