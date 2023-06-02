package client.rapid.module.modules.player;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Auto Respawn", getCategory = Category.PLAYER)
public class AutoRespawn extends Module {
	private final Setting fast = new Setting("Fast", this, true);

	public AutoRespawn() {
		add(fast);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(fast.isEnabled() && mc.thePlayer.getHealth() == 0)
					mc.thePlayer.respawnPlayer();
			else {
				if(mc.thePlayer.isDead)
					mc.thePlayer.respawnPlayer();
			}
		}
	}
}
