package client.rapid.event.events.player;

import client.rapid.event.events.Event;
import client.rapid.util.module.RotationUtil;

public class EventMotion extends Event {
	public float yaw, pitch;
	private double x, y, z;
	private boolean ground;
	
	public EventMotion(float yaw, float pitch, double x, double y, double z, boolean ground) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ground = ground;
	}

	public float getYaw() {
		return yaw;
	}
	
	public void setYaw(float yaw) {
		RotationUtil.yaw = yaw;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		RotationUtil.pitch = pitch;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public boolean isGround() {
		return ground;
	}
	
	public void setGround(boolean ground) {
		this.ground = ground;
	}

}
