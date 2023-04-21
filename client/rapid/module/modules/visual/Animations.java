package client.rapid.module.modules.visual;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Animations", getCategory = Category.VISUAL)
public class Animations extends Module {
	private final Setting
	mode = new Setting("Mode", this, "1.8", "1.7", "Basic", "Slide", "Rotate", "Spin", "Exhibition", "Geuxy", "What", "Eumel"),
	swingSpeed = new Setting("Swing Speed", this, 6, 2, 50, true),
	itemSize = new Setting("Item Size", this, 0.2, 0, 0.4, false),
	betterSwing = new Setting("Better Swing", this, false);

	public Animations() {
		add(mode, swingSpeed, itemSize, betterSwing);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion && e.isPre())
			setTag(mode.getMode());
	}
}
