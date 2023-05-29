package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.module.modules.movement.speeds.SpeedMode;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.*;

import java.util.*;

@ModuleInfo(getName = "Speed", getCategory = Category.MOVEMENT)
public class Speed extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "NCP", "Vulcan", "Verus");

	private final Setting vanillaMode = new Setting("Vanilla", this, "Motion", "Strafe");
	private final Setting ncpMode = new Setting("NCP", this, "NCP Hop", "NCP LowHop", "NCP YPort");
	private final Setting verusMode = new Setting("Verus", this, "Verus Hop", "Verus Ground");
	private final Setting vulcanMode = new Setting("Vulcan", this, "Vulcan Hop", "Vulcan LowHop", "Vulcan Ground");

	private final Setting speed = new Setting("Speed", this, 12, 1, 100, true);
	private final Setting damageBoost = new Setting("Damage Boost", this, 0.01, 0, 2, false);
	private final Setting groundStrafe = new Setting("Ground Strafe", this, false);
	private final Setting disableOnFlag = new Setting("Disable on flag", this, false);

	private final List<Vec3> lastLocations = new ArrayList<>();

	private SpeedBase currentMode;

	public Speed() {
		add(mode, vanillaMode, ncpMode, verusMode, vulcanMode, speed, damageBoost, groundStrafe, disableOnFlag);
		this.setMode();
	}

	public void settingCheck() {
		groundStrafe.setVisible(mode.getMode().equals("Vulcan") || (mode.getMode().equals("Vanilla") && vanillaMode.getMode().equals("Strafe")));
		damageBoost.setVisible(!mode.getMode().equals("Vanilla") && !mode.getMode().equals("Vulcan"));
		speed.setVisible(mode.getMode().equals("Vanilla"));
		vanillaMode.setVisible(mode.getMode().equals("Vanilla"));
		ncpMode.setVisible(mode.getMode().equals("NCP"));
		vulcanMode.setVisible(mode.getMode().equals("Vulcan"));
		verusMode.setVisible(mode.getMode().equals("Verus"));
	}

	@Override
	public void onEnable() {
		this.setMode();
		currentMode.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
        mc.thePlayer.speedInAir = 0.02f;
		mc.thePlayer.jumpMovementFactor = 0.02f;
		currentMode.onDisable();
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		this.setMode();

		if(mc.thePlayer.onGround && mc.gameSettings.keyBindJump.isKeyDown()) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
		}

		if(mc.thePlayer.isInWater())
			return;

		currentMode.onEvent(e);

		// Disable on Lagback.
		if(e instanceof EventMotion && e.isPost())
			lastLocations.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket) e;
			if (event.getPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook p = event.getPacket();

				if (lastLocations.stream().anyMatch(loc -> p.getX() == loc.xCoord && p.getY() == loc.yCoord && p.getZ() == loc.zCoord) && disableOnFlag.isEnabled()) {
					NotificationManager.addToQueue(new Notification("Lagback", "Disabled Speed for Flagging!", NotificationType.WARNING, 3));
					setEnabled(false);
				}
			}
		}
	}

	private void setMode() {
		for(SpeedMode sm : SpeedMode.values()) {
			if(currentMode != sm.getBase()) {
				switch(mode.getMode()) {
					case "Vanilla":
						if (vanillaMode.getMode().equals(sm.getName())) {
							currentMode = sm.getBase();
						}
						break;
					case "NCP":
						if (ncpMode.getMode().equals(sm.getName())) {
							currentMode = sm.getBase();
						}
						break;
					case "Vulcan":
						if (vulcanMode.getMode().equals(sm.getName())) {
							currentMode = sm.getBase();
						}
						break;
					case "Verus":
						if (verusMode.getMode().equals(sm.getName())) {
							currentMode = sm.getBase();
						}
						break;
				}
			}
		}
	}

}
