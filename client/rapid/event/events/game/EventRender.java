package client.rapid.event.events.game;

import client.rapid.event.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender extends Event {
	private final ScaledResolution sr;
	
	public EventRender(ScaledResolution sr) {
		this.sr = sr;
	}
	
	public ScaledResolution getScaledResolution() {
		return sr;
	}
}
