package client.rapid.event.events.player;

import client.rapid.event.Event;

// Hooked in EntityPlayerSP.java
public class EventUpdate extends Event {

    private final float yaw;
    private final float pitch;

    public EventUpdate(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

}
