package client.rapid.module.modules.movement.flights.loyisa;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.flights.FlightBase;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.module.MoveUtil;

public class VenomFlight extends FlightBase {

    @Override
    public void onEnable() {
        NotificationManager.add("Flight", "Use Verus No Fall on 3.0 distance", NotificationType.INFO, 3);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.hurtTime != 0) {
                mc.thePlayer.motionY = 0.42;
                MoveUtil.setMoveSpeed(0.7);
            }
        }
    }
}
