package client.rapid.event;

import client.rapid.Client;
import client.rapid.event.events.game.EventChat;
import client.rapid.module.Module;

import net.minecraft.client.Minecraft;

public class Event {

	private EventType type;
	private boolean cancelled;
	
	public void callEvent() {
		if(this instanceof EventChat && this.isPre()) {
			Client.getInstance().getCommandManager().handleChat((EventChat) this);
		}

		// Handle events for modules.
		if(Minecraft.getMinecraft().thePlayer != null) {
			Client.getInstance().getModuleManager().getModules().stream()
				.filter(Module::isEnabled)
				.forEach(m -> m.onEvent(this));
		}

	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	public boolean isPre() {
		return type != null && type == EventType.PRE;
	}
	
	public boolean isPost() {
		return type != null && type == EventType.POST;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void cancel() {
		this.cancelled = true;
	}

}
