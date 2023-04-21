package client.rapid.event.events.player;

import client.rapid.event.events.Event;

public class EventUpdate extends Event {
    private float yaw, pitch;

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
