package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import net.minecraft.client.settings.KeyBinding;

@ModuleInfo(getName = "Sprint", getCategory = Category.MOVEMENT)
public class Sprint extends Module {
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(isEnabled("Scaffold") && !getBoolean("Scaffold", "Sprint"))
				return;

			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
		}
	}
}
