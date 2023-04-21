package client.rapid.event.events.player;

import client.rapid.event.events.Event;

public class EventStrafe extends Event {
    float strafe, forward, friction, yaw;

    public EventStrafe(float strafe, float forward, float friction, float yaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }

    public float getForward() {
        return forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getFriction() {
        return friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
