package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Fast Ladder", getCategory = Category.MOVEMENT)
public class FastLadder extends Module {
	private final Setting
	mode = new Setting("Mode", this, "Vanilla", "Timer", "Clip"),
	speed = new Setting("Speed", this, 1.2, 1.01, 5, false);

	public FastLadder() {
		add(mode, speed);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());
			if(mc.thePlayer.isOnLadder() && isMoving() && !mc.thePlayer.onGround && !(mc.thePlayer.fallDistance > 0)) {
				switch(mode.getMode()) {
					case "Vanilla":
						mc.thePlayer.motionY *= speed.getValue();
						break;
					case "Timer":
						mc.timer.timerSpeed = (float)speed.getValue();
						break;
					case "Clip":
						mc.thePlayer.sendChatMessage(".vclip " + speed.getValue());
						break;
				}
			} else
				mc.timer.timerSpeed = 1f;
		}
	}
}
