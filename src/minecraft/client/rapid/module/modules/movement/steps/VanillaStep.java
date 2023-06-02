package client.rapid.module.modules.movement.steps;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventStep;

public class VanillaStep extends StepBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventStep && e.isPre()) {
            EventStep event = (EventStep) e;

            if(mc.thePlayer.isCollidedHorizontally) {
                event.setHeight((float) height.getValue());
                timer.reset();
            } else {
                event.setHeight(0.6f);
            }
        }
    }
}
