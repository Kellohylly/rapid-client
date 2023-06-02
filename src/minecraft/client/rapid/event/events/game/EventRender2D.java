package client.rapid.event.events.game;

import client.rapid.event.Event;

import net.minecraft.client.gui.ScaledResolution;

// Hooked in GuiInGame.java
public class EventRender2D extends Event {
	private final ScaledResolution sr;
	
	public EventRender2D(ScaledResolution sr) {
		this.sr = sr;
	}
	
	public ScaledResolution getScaledResolution() {
		return sr;
	}

}
