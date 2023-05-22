package client.rapid.event.events;

import client.rapid.Wrapper;
import client.rapid.event.*;
import client.rapid.event.events.game.EventChat;
import client.rapid.module.Module;
import net.minecraft.client.Minecraft;

public class Event extends EventCancel {
	private EventType type;
	
	public static void dispatch(Event e) {
		if(e instanceof EventChat && e.isPre())
			Wrapper.getCommandManager().handle((EventChat)e);
		
		if(Minecraft.getMinecraft().thePlayer != null) {
			Wrapper.getModuleManager().getModules().stream().filter(Module::isEnabled).forEach(m -> m.onEvent(e));
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
}
