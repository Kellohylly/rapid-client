package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Custom Speed", getCategory = Category.MOVEMENT)
public class CustomSpeed extends Module {
	private final Setting
	jumpHeight = new Setting("Jump Height", this, 0.42, 0, 1, false),
	groundSpeed = new Setting("Ground Speed", this, 0.49, 0, 1, false),
	fallMode = new Setting("Fall", this, "Normal", "Ticks", "Y Port"),
	fastFall = new Setting("Fast Fall", this, 0, 0, 1, false),
	fallTicks = new Setting("Fall Ticks", this, 6, 1, 8, true),
	airSpeed = new Setting("Air Speed", this, 0.49, 0, 1, false),
	slowdown = new Setting("Slowdown", this, 0.05, 0, 0.1, false),
	timer = new Setting("Timer", this, 1, 0.1, 2, false);
	
	private double moveSpeed;
	private boolean changeSpeed;
	private int ticks;

	public CustomSpeed() {
		add(jumpHeight, groundSpeed, fallMode, fastFall, fallTicks, airSpeed, slowdown, timer);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(isMoving()) {
				ticks++;

				if(mc.thePlayer.onGround) {
					if(jumpHeight.getValue() != 0)
						mc.thePlayer.jump();

					ticks = 0;
					
					mc.thePlayer.motionY = jumpHeight.getValue();
					moveSpeed = groundSpeed.getValue();
					changeSpeed = true;
				} else {
					if(ticks == (int)fallTicks.getValue() && fallMode.getMode().equals("Ticks"))
						mc.thePlayer.motionY = -fastFall.getValue();

					if(mc.thePlayer.fallDistance > 0 && fallMode.getMode().equals("Normal"))
						mc.thePlayer.motionY -= fastFall.getValue();
					
					if(fallMode.getMode().equals("Y Port") && changeSpeed)
						mc.thePlayer.motionY = -fastFall.getValue();
					
					if(moveSpeed >= 0.2)
						moveSpeed -= slowdown.getValue();

					if(changeSpeed) {
						moveSpeed = airSpeed.getValue() == 0 ? getMoveSpeed() : airSpeed.getValue();
						changeSpeed = false;
					}
				}
				setMoveSpeed(moveSpeed);
			}
			mc.timer.timerSpeed = (float)timer.getValue();
		}
	}
}
