package client.rapid.module.modules.visual;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.*;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Full Bright", getCategory = Category.VISUAL)
public class FullBright extends Module {
	private float old;
	
	public void onEnable() {
		old = mc.gameSettings.gammaSetting;
	}
	
	public void onDisable() {
		mc.gameSettings.gammaSetting = old;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion && e.isPre())
			mc.gameSettings.gammaSetting = 99;
	}
}
