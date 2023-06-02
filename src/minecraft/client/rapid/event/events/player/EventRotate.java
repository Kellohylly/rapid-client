package client.rapid.event.events.player;

import client.rapid.event.Event;

// Hooked in EntityRenderer.java
public class EventRotate extends Event {

	private float curYaw;
	private float curPitch;
	private float deltaX;
	private float deltaY;

    private boolean stopRotate;
    
	public EventRotate(float curYaw, float curPitch, float deltaX, float deltaY, boolean stopRotate) {
		this.curYaw = curYaw;
		this.curPitch = curPitch;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.stopRotate = stopRotate;
	}

	public float getCurYaw() {
		return curYaw;
	}

	public void setCurYaw(float curYaw) {
		this.curYaw = curYaw;
	}

	public float getCurPitch() {
		return curPitch;
	}

	public void setCurPitch(float curPitch) {
		this.curPitch = curPitch;
	}

	public float getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(float deltaX) {
		this.deltaX = deltaX;
	}

	public float getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(float deltaY) {
		this.deltaY = deltaY;
	}

	public boolean isStopRotate() {
		return stopRotate;
	}

	public void setStopRotate(boolean stopRotate) {
		this.stopRotate = stopRotate;
	}

}