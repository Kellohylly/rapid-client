package client.rapid.module.modules.movement.flights.vanilla;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.flights.FlightBase;

public class CreativeFlight extends FlightBase {

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            mc.thePlayer.capabilities.isFlying = true;
        }
    }
}
