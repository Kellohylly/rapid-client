package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.MoveUtil;

@ModuleInfo(getName = "Custom Speed", getCategory = Category.MOVEMENT)
public class CustomSpeed extends Module {
	private final Setting jumpHeight = new Setting("Jump Height", this, 0.42, 0, 1, false);
	private final Setting groundSpeed = new Setting("Ground Speed", this, 0.49, 0, 1, false);
	private final Setting fallMode = new Setting("Fall", this, "Normal", "Ticks", "Y Port");
	private final Setting fastFall = new Setting("Fast Fall", this, 0, 0, 1, false);
	private final Setting fallTicks = new Setting("Fall Ticks", this, 6, 1, 8, true);
	private final Setting airSpeed = new Setting("Air Speed", this, 0.49, 0, 1, false);
	private final Setting slowdown = new Setting("Slowdown", this, 0.05, 0, 0.1, false);
	private final Setting timer = new Setting("Timer", this, 1, 0.1, 2, false);
	
	private double moveSpeed;
	private boolean changeSpeed;
	private int ticks;

	public CustomSpeed() {
		add(jumpHeight, groundSpeed, fallMode, fastFall, fallTicks, airSpeed, slowdown, timer);
	}

	@Override
	public void updateSettings() {
		fallTicks.setVisible(fallMode.getMode().equals("Ticks"));
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(MoveUtil.isMoving()) {
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
						moveSpeed = airSpeed.getValue() == 0 ? MoveUtil.getMoveSpeed() : airSpeed.getValue();
						changeSpeed = false;
					}
				}
				MoveUtil.setMoveSpeed(moveSpeed);
			}
			mc.timer.timerSpeed = (float)timer.getValue();
		}
	}
}
