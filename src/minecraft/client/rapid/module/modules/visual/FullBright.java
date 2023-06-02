package client.rapid.module.modules.visual;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Full Bright", getCategory = Category.VISUAL)
public class FullBright extends Module {
	private float old;

	@Override
	public void onEnable() {
		old = mc.gameSettings.gammaSetting;
	}

	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = old;
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventMotion && e.isPre())
			mc.gameSettings.gammaSetting = 99;
	}
}
