package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventWorldLoad;
import client.rapid.event.events.player.EventMove;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.longjumps.LongJumpBase;
import client.rapid.module.modules.movement.longjumps.LongJumpMode;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;

@ModuleInfo(getName = "Long Jump", getCategory = Category.MOVEMENT)
public class LongJump extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "NCP", "Vulcan");

	private final Setting ncpMode = new Setting("NCP", this, "Old NCP", "NCP Dev");
	private final Setting vulcanMode = new Setting("Vulcan", this, "Vulcan High", "Vulcan Clip");

	private final Setting damage = new Setting("Damage", this, "None", "Simple", "Jump", "Wait");
	private final Setting speed = new Setting("Speed", this, 1.6, 0.2, 10, false);
	private final Setting height = new Setting("Height", this, 1.6, 0.2, 10, false);
	private final Setting slowdown = new Setting("Slowdown Speed", this, 0.02, 0.01, 0.2, false);
	private final Setting autoDisable = new Setting("Auto Disable", this, false);

	private LongJumpBase currentMode;

	private boolean damaged;
	private boolean jumped;
	private int ticks;

	public LongJump() {
		add(mode, ncpMode, vulcanMode, damage, speed, height, slowdown, autoDisable);
	}

	@Override
	public void updateSettings() {
		height.setVisible(mode.getMode().equals("Vulcan") || mode.getMode().equals("Vanilla") || mode.getMode().equals("Vulcan2"));
		slowdown.setVisible(mode.getMode().equals("NCP") && ncpMode.getMode().equals("Old NCP"));
		speed.setVisible(mode.getMode().equals("Vanilla") || (mode.getMode().equals("NCP") && ncpMode.getMode().equals("Old NCP")));
		ncpMode.setVisible(mode.getMode().equals("NCP"));
		vulcanMode.setVisible(mode.getMode().equals("Vulcan"));
	}

	@Override
	public void onEnable() {
		damaged = mc.thePlayer.hurtTime != 0;
		jumped = false;
		ticks = 0;

		this.setMode();

		currentMode.onEnable();
	}

	@Override
	public void onDisable() {
		jumped = false;
		mc.timer.timerSpeed = 1f;
		mc.thePlayer.speedInAir = 0.02f;
		ticks = 0;
		damaged = false;

		currentMode.onDisable();
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		this.setMode();

		if(e instanceof EventMove) {
			EventMove event = (EventMove) e;
			if(!damage.getMode().equals("None") && !damaged) {
				event.setX(0);
				event.setZ(0);
			}
		}

		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.thePlayer.hurtTime != 0) {
				damaged = true;
			}

			if(autoDisable.isEnabled() && mc.thePlayer.getHealth() <= 0) {
				setEnabled(false);
			}
		}
		if(e instanceof EventWorldLoad && e.isPre() && autoDisable.isEnabled()) {
			setEnabled(false);
		}

		if(!damaged && !damage.getMode().equals("None")) {
			if(e instanceof EventUpdate && e.isPre()) {
				MoveUtil.setMoveSpeed(0);
				switch(damage.getMode()) {
					case "Simple":
						PlayerUtil.damagePlayer(3.001F);
						damaged = true;
						break;
					case "Jump":
						if (ticks < 3) {
							if (mc.thePlayer.onGround) {
								mc.thePlayer.onGround = false;
								mc.thePlayer.jump();
								ticks++;
							}
							break;
						}
				}
			}
			return;
		}

		// holy fuck this is disgusting
		if(autoDisable.isEnabled()) {
			if(jumped) {

				if(e instanceof EventUpdate && e.isPre()) {
					ticks++;
				}

				if(mc.thePlayer.onGround && !currentMode.isSpoofing()) {
					MoveUtil.setMoveSpeed(0);
					this.setEnabled(false);
				}
			} else {
				if(!mc.thePlayer.onGround) {
					jumped = true;
				}
			}
		}

		currentMode.updateValues();
		currentMode.onEvent(e);

	}

	private void setMode() {
		for(LongJumpMode lm : LongJumpMode.values()) {
			if(currentMode != lm.getBase()) {
				switch(mode.getMode()) {
					case "Vanilla":
						if (mode.getMode().equals(lm.getName())) {
							currentMode = lm.getBase();
						}
						break;
					case "NCP":
						if (ncpMode.getMode().equals(lm.getName())) {
							currentMode = lm.getBase();
						}
						break;
					case "Vulcan":
						if (vulcanMode.getMode().equals(lm.getName())) {
							currentMode = lm.getBase();
						}
						break;
				}
			}
		}
	}

}