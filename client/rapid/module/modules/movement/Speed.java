package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.*;

@ModuleInfo(getName = "Speed", getCategory = Category.MOVEMENT)
public class Speed extends Module {
	private final Setting
	mode = new Setting("Mode", this, "Vanilla", "Strafe", "NCP", "Updated NCP", "Vulcan", "Verus", "Watchdog"),
	speed = new Setting("Speed", this, 12, 1, 100, true),
	damageBoost = new Setting("Damage Boost", this, 0.01, 0, 2, false),
	groundStrafe = new Setting("Ground Strafe", this, false),
	disableOnFlag = new Setting("Disable on flag", this, false);

	private int jumps, ticks;
	private double launchY;
	private boolean vulcanFix;

	private final List<Vec3> lastLocations = new ArrayList<>();

	public Speed() {
		add(mode, speed, damageBoost, groundStrafe, disableOnFlag);
	}
	
	public void onEnable() {
		vulcanFix = !mc.thePlayer.onGround;
		jumps = 0;
		ticks = 0;
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
        mc.thePlayer.speedInAir = 0.02f;
		mc.thePlayer.jumpMovementFactor = 0.02f;
		jumps = 0;
		ticks = 0;
		launchY = 0;
		vulcanFix = false;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());
			if(isMoving())
				ticks++;

			if(mc.thePlayer.onGround) {
				if(isMoving())
					ticks = 0;

				if(mc.gameSettings.keyBindJump.isKeyDown())
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
				jumps++;
			}

			if(mc.thePlayer.isInWater())
				return;

			switch(mode.getMode()) {
			case "Vanilla":
				if(isMovingOnGround())
					mc.thePlayer.jump();
				
				setMoveSpeed(speed.getValue() / 10);
				break;
			case "Strafe":
				if(isMovingOnGround()) {
					mc.thePlayer.jump();
					setMoveSpeed(getMoveSpeed());

				} else {
					if(!groundStrafe.isEnabled())
						setMoveSpeed(getMoveSpeed());
				}
				break;
				case "NCP":
					if(isMovingOnGround()) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY -= 0.011f;
						if(mc.thePlayer.moveForward != 0) {
							mc.thePlayer.motionX *= 1.016D;
							mc.thePlayer.motionZ *= 1.016D;
							mc.thePlayer.speedInAir = 0.022f;
						}
						mc.timer.timerSpeed = 1.15f;
					} else {
						if(ticks == 7)
							mc.thePlayer.motionY = -0.16f;

						if(mc.thePlayer.fallDistance > 0)
							mc.timer.timerSpeed = 1.1f;
						else
							mc.timer.timerSpeed = 1.04f;
					}
					if(mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0)
						setMoveSpeed(getBaseMoveSpeed() / 1.08);

					setMoveSpeed(getMoveSpeed() + (mc.thePlayer.hurtTime > 0 ? damageBoost.getValue() : 0));
					break;
			case "Updated NCP":
				if(mc.thePlayer.onGround && isMoving()) {
					mc.thePlayer.jump();
					mc.thePlayer.motionY -= 0.01f;
					mc.timer.timerSpeed = 1.1f;
				} else {
					mc.timer.timerSpeed = 1.065f;

					if(ticks == 6)
						mc.thePlayer.motionY = -0.17;

					if(mc.thePlayer.fallDistance > 0)
						mc.thePlayer.motionY -= 0.001f;
				}
				mc.thePlayer.motionY -= 0.002f;
				setMoveSpeed(getMoveSpeed() + (mc.thePlayer.hurtTime > 0 ? damageBoost.getValue() : 0));

				if(mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0)
					setMoveSpeed(getBaseMoveSpeed() / 1.1);
				break;
			case "Vulcan":
				if(isMoving()) {
					if(isMovingOnGround()) {
						mc.timer.timerSpeed = 1.1f;
						mc.thePlayer.jump();
						if(mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0)
							setMoveSpeed(getBaseMoveSpeed() * 1.8);
						else
							setMoveSpeed(getMoveSpeed());

						vulcanFix = false;
						
						launchY = mc.thePlayer.posY;
					} else {
						mc.timer.timerSpeed = 1f;

						if(!vulcanFix) {
							if(mc.thePlayer.chasingPosY > launchY && ticks <= 1)
								mc.thePlayer.setPosition(mc.thePlayer.posX, launchY, mc.thePlayer.posZ);
							else if(ticks == 6)
								mc.thePlayer.motionY = -0.3;
						}
					}
					mc.thePlayer.jumpMovementFactor = 0.0245f;

					if(!groundStrafe.isEnabled())
						setMoveSpeed(getMoveSpeed());
				}
				break;
				case "Verus":
					double moveSpeed = mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0 ? 0.32 : (mc.thePlayer.isSprinting() ? 0.37 : 0.34);

					if(mc.thePlayer.isSneaking())
						return;

					if(isMovingOnGround()) {
						mc.thePlayer.jump();
						moveSpeed = mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0 ? 0.4 : (mc.thePlayer.isSprinting() || mc.thePlayer.isSneaking() ? 0.54 : 0.51);
					}

					if (PlayerUtil.hasEffect(Potion.moveSpeed))
						moveSpeed *= 1.0 + 0.17 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

					if(PlayerUtil.hasEffect(Potion.moveSlowdown))
						moveSpeed *= 0.9f;

					if(damageBoost.getValue() > 0 && mc.thePlayer.hurtTime > 0)
						moveSpeed = moveSpeed + damageBoost.getValue();

					setMoveSpeed(moveSpeed);
					break;
				case "Watchdog":
					if(mc.thePlayer.onGround)
						mc.thePlayer.jump();

					if(mc.thePlayer.hurtTime > 0)
						setMoveSpeed(getMoveSpeed() + damageBoost.getValue() / 10);
					break;
			}
		}
		if(e instanceof EventMotion && e.isPost())
			lastLocations.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket) e;
			if (event.getPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook p = event.getPacket();

				if (lastLocations.stream().anyMatch(loc -> p.getX() == loc.xCoord && p.getY() == loc.yCoord && p.getZ() == loc.zCoord) && disableOnFlag.isEnabled()) {
					PlayerUtil.addChatMessage(EnumChatFormatting.GRAY + "Disabled " + EnumChatFormatting.RED + getName() + EnumChatFormatting.GRAY + " for flagging!");
					setEnabled(false);
				}
			}
		}
	}

}
