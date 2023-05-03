package client.rapid.event.events.player;

import client.rapid.event.events.Event;

public class EventJump extends Event {
    private float yaw;

    public EventJump(float yaw) {

        this.yaw = yaw;
    }
    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
