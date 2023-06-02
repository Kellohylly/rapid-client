package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.MoveUtil;

@ModuleInfo(getName = "Fast Ladder", getCategory = Category.MOVEMENT)
public class FastLadder extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Timer");
	private final Setting speed = new Setting("Speed", this, 1.2, 1.01, 5, false);

	public FastLadder() {
		add(mode, speed);
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());
			if(mc.thePlayer.isOnLadder() && MoveUtil.isMoving() && !mc.thePlayer.onGround && !(mc.thePlayer.fallDistance > 0)) {
				switch(mode.getMode()) {
					case "Vanilla":
						mc.thePlayer.motionY *= speed.getValue();
						break;
					case "Timer":
						mc.timer.timerSpeed = (float)speed.getValue();
						break;
				}
			} else
				mc.timer.timerSpeed = 1f;
		}
	}
}
