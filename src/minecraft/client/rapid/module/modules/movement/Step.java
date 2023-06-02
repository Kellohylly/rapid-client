package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.steps.StepBase;
import client.rapid.module.modules.movement.steps.StepMode;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;

@ModuleInfo(getName = "Step", getCategory = Category.MOVEMENT)
public class Step extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Old NCP", "Motion");

	private final Setting motionMode = new Setting("Motion", this, "NCP", "Matrix", "Karhu");

	private final Setting height = new Setting("Height", this, 1.5, 1, 2, false);
	private final Setting delay = new Setting("Delay", this, 0, 0, 600, true);

	public final TimerUtil timer = new TimerUtil();

	private boolean stepped;

	private StepBase currentMode;

	public Step() {
		add(mode, motionMode, height, delay);
	}

	@Override
	public void updateSettings() {
		height.setVisible(mode.getMode().equals("Vanilla") || mode.getMode().equals("Packet"));
		motionMode.setVisible(mode.getMode().equals("Motion"));
	}

	@Override
	public void onEnable() {
		this.setMode();
		currentMode.onEnable();
	}

	@Override
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.6F;
		stepped = false;
		currentMode.onDisable();
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		this.setMode();

		if(timer.reached(delay.getValue())) {
			currentMode.onEvent(e);
		}
	}

	private void setMode() {
		for(StepMode fm : StepMode.values()) {
			if(currentMode != fm.getBase()) {
				switch(mode.getMode()) {
					case "Vanilla":
						if (mode.getMode().equals("Vanilla")) {
							currentMode = fm.getBase();
						}
						break;
					case "Old NCP":
						if (mode.getMode().equals("Old NCP")) {
							currentMode = fm.getBase();
						}
						break;
					case "Motion":
						if (motionMode.getMode().equals(fm.getName())) {
							currentMode = fm.getBase();
						}
						break;
				}
			}
		}
	}
}
