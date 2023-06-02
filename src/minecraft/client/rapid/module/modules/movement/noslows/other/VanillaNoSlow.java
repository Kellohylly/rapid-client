package client.rapid.module.modules.movement.noslows.other;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventSlowdown;
import client.rapid.module.modules.movement.noslows.NoSlowBase;

public class VanillaNoSlow extends NoSlowBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSlowdown) {
            EventSlowdown event = (EventSlowdown) e;

            event.cancel();
        }
    }

}
