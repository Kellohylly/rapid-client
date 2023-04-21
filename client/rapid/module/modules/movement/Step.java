package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;

@ModuleInfo(getName = "Step", getCategory = Category.MOVEMENT)
public class Step extends Module {
	private final Setting
	mode = new Setting("Mode", this, "Vanilla", "Matrix", "Ground"),
	height = new Setting("Height", this, 1.5, 1, 2, false),
	delay = new Setting("Delay", this, 0, 0, 600, true);

	private final TimerUtil timer = new TimerUtil();

	private boolean stepped;

	public Step() {
		add(mode, height, delay);
	}

	public void onDisable() {
		mc.thePlayer.stepHeight = 0.6F;
		stepped = false;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());

			if(!mode.getMode().equals("Vanilla"))
				mc.thePlayer.stepHeight = 0.6F;
			
			if(mc.thePlayer.isInWater() || mc.thePlayer.isOnLadder())
				return;

			if(!timer.sleep((int)delay.getValue()) && !stepped && mc.thePlayer.isCollidedHorizontally)
				return;

			switch(mode.getMode()) {
			case "Vanilla":
				mc.thePlayer.stepHeight = (float) height.getValue();
				break;
			case "Matrix":
				if (mc.thePlayer.isCollidedHorizontally && isMovingOnGround()) {
					mc.thePlayer.jump();
					stepped = true;
				} else {
					if (!mc.thePlayer.isCollidedHorizontally && stepped) {
						mc.thePlayer.motionY = 0;
						stepped = false;
					}
				}
				break;
			case "Ground":
				if (mc.thePlayer.isCollidedHorizontally && isMoving() && mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					stepped = true;
				} else {
					if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.onGround && stepped) {
						mc.thePlayer.onGround = true;
						stepped = false;
					}
				}
				break;
			}
		}
	}
}
