package client.rapid.event.events.player;

import client.rapid.event.events.Event;

public class EventRotation extends Event {
    private float yaw, pitch;

	public EventRotation(float yaw, float pitch) {
		super();
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
    
    
}