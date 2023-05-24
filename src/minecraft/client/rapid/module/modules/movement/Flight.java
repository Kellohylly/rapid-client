package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.flights.FlightBase;
import client.rapid.module.modules.movement.flights.FlightMode;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import client.rapid.util.TimerUtil;

@ModuleInfo(getName = "Flight", getCategory = Category.MOVEMENT)
public class Flight extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Old NCP", "Verus");

	private final Setting vanillaMode = new Setting("Vanilla", this, "Motion", "Creative");
	private final Setting verusMode = new Setting("Verus", this, "Collide", "Verus Fast");

	private final Setting damage = new Setting("Damage", this, "None", "Simple", "Jump", "Wait", "Vulcant");
	private final Setting speed = new Setting("Speed", this, 2, 0.2, 10, false);
	private final Setting fast = new Setting("Fast", this, false);
	private final Setting jump = new Setting("Jump", this, false);
	private final Setting bobbing = new Setting("Bobbing", this, true);

	private FlightBase currentMode;

	public static boolean damaged;
	private int ticks;

	private final TimerUtil timer = new TimerUtil();

	public Flight() {
		add(mode, vanillaMode, verusMode, damage, speed, fast, jump, bobbing);
	}

	@Override
	public void settingCheck() {
		speed.setVisible((mode.getMode().equals("Vanilla") && vanillaMode.getMode().equals("Motion")) || mode.getMode().equals("Old NCP") || (mode.getMode().equals("Verus") && verusMode.getMode().equals("Verus Fast")));
		fast.setVisible((mode.getMode().equals("Verus") && verusMode.getMode().equals("Verus Fast")) || mode.getMode().equals("Old NCP"));
		bobbing.setVisible(mode.getMode().equals("Old NCP"));
		jump.setVisible((mode.getMode().equals("Verus") && verusMode.getMode().equals("Collide")));
		verusMode.setVisible(mode.getMode().equals("Verus"));
		vanillaMode.setVisible(mode.getMode().equals("Vanilla"));
	}

	@Override
	public void onEnable() {
		this.setMode();
		currentMode.onEnable();
	}

	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		mc.timer.timerSpeed = 1f;
		setMoveSpeed(getMoveSpeed() / 5);

		damaged = false;
		ticks = 0;
		currentMode.onDisable();
	}

	@Override
	public void onEvent(Event e) {
		this.setMode();

		this.setTag(mode.getMode());

		if(e instanceof EventUpdate && e.isPre()) {
			if(bobbing.isEnabled()) {
				mc.thePlayer.cameraYaw = 0.0696f;
			}

			if (mc.thePlayer.hurtTime != 0) {
				damaged = true;
			}
		}
		autoDisable(e);

		if(!damaged && !damage.getMode().equals("None")) {
			if(e instanceof EventUpdate && e.isPre()) {
				setMoveSpeed(0);
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
						}
						break;
					case "Vulcant":
						if (mc.thePlayer.onGround) {
							if(ticks == 1)
								mc.thePlayer.jump();
						}

						if(ticks == 8)
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 10, mc.thePlayer.posZ);

						ticks++;
						break;
				}
			}
			return;
		}

		currentMode.updateValues();
		currentMode.onEvent(e);
	}

	private void setMode() {
		for(FlightMode fm : FlightMode.values()) {
			if(currentMode != fm.getBase()) {
				switch(mode.getMode()) {
					case "Vanilla":
						if (vanillaMode.getMode().equals(fm.getName())) {
							currentMode = fm.getBase();
						}
						break;
					case "Old NCP":
						if (mode.getMode().equals(fm.getName())) {
							currentMode = fm.getBase();
						}
						break;
					case "Verus":
						if (verusMode.getMode().equals(fm.getName())) {
							currentMode = fm.getBase();
						}
						break;
				}
			}
		}
	}

}
