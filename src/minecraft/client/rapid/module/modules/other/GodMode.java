package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;

@ModuleInfo(getName = "God Mode", getCategory = Category.OTHER)
public class GodMode extends Module {
	private final Setting health = new Setting("Health", this, 10, 1, 19, true);
	private final Setting delay = new Setting("Delay", this, 500, 10, 1000, true);

	private final TimerUtil timer = new TimerUtil();

	public GodMode() {
		add(health, delay);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.thePlayer.getHealth() <= health.getValue() && timer.sleep((int)delay.getValue()))
				mc.thePlayer.sendChatMessage("/heal");
		}
	}
}
