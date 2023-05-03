package client.rapid.event.events.player;

import client.rapid.event.events.Event;
import net.minecraft.entity.Entity;

public class EventStep extends Event {
	private Entity entity;
	private float height;

	public EventStep(final Entity entity) {
		this.entity = entity;
		this.height = entity.stepHeight;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
