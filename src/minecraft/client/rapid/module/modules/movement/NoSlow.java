package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.noslows.NoSlowBase;
import client.rapid.module.modules.movement.noslows.NoSlowMode;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "No Slow", getCategory = Category.MOVEMENT)
public class NoSlow extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "NCP");

	private final Setting ncpMode = new Setting("NCP", this, "Old NCP", "Updated NCP");

	private final Setting delay = new Setting("Delay", this, 60, 1, 100, true);
	private final Setting allowSprinting = new Setting("Allow Sprinting", this, true);

	private NoSlowBase currentMode;

	public NoSlow() {
		add(mode, ncpMode, delay, allowSprinting);
	}

	@Override
	public void onEnable() {
		this.setMode();
		currentMode.onEnable();
	}

	@Override
	public void onDisable() {
		currentMode.onDisable();
	}

	@Override
	public void updateSettings() {
		delay.setVisible(!mode.getMode().equals("Vanilla"));
		ncpMode.setVisible(mode.getMode().equals("NCP"));
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		this.setMode();

		currentMode.onEvent(e);

		if(e instanceof EventUpdate) {
			if (mc.thePlayer.isUsingItem()) {
				if(mc.thePlayer.isSprinting() && !allowSprinting.isEnabled()) {
					mc.thePlayer.setSprinting(false);
				}
			}
		}
	}

	private void setMode() {
		for(NoSlowMode fm : NoSlowMode.values()) {
			if(currentMode != fm.getBase()) {
				switch(mode.getMode()) {
					case "Vanilla":
						if (mode.getMode().equals("Vanilla")) {
							currentMode = NoSlowMode.VANILLA.getBase();
						}
						break;
					case "NCP":
						if (ncpMode.getMode().equals(fm.getName())) {
							currentMode = fm.getBase();
						}
						break;
				}
			}
		}
	}
}
