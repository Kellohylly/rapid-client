package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.util.PlayerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.*;

@ModuleInfo(getName = "Speed", getCategory = Category.MOVEMENT)
public class Speed extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Strafe", "NCP", "Old NCP", "Vulcan", "Verus", "Verus Ground");
	private final Setting speed = new Setting("Speed", this, 12, 1, 100, true);
	private final Setting damageBoost = new Setting("Damage Boost", this, 0.01, 0, 2, false);
	private final Setting groundStrafe = new Setting("Ground Strafe", this, false);
	private final Setting disableOnFlag = new Setting("Disable on flag", this, false);

	private int ticks;

	private final List<Vec3> lastLocations = new ArrayList<>();

	public Speed() {
		add(mode, speed, damageBoost, groundStrafe, disableOnFlag);
	}

	@Override
	public void onEnable() {
		ticks = 0;

		if(mode.getMode().equals("Verus Ground") && mc.thePlayer.onGround)
			mc.thePlayer.jump();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
        mc.thePlayer.speedInAir = 0.02f;
		mc.thePlayer.jumpMovementFactor = 0.02f;
		ticks = 0;
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());
				ticks++;

			if(mc.thePlayer.onGround) {
				if(isMoving())
					ticks = 0;

				if(mc.gameSettings.keyBindJump.isKeyDown())
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
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
					strafe();
				} else {
					if(!groundStrafe.isEnabled())
						strafe();
				}
				break;
				case "Old NCP":
					if(isMoving()) {
						if (mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							mc.thePlayer.motionX *= 1.17D;
							mc.thePlayer.motionZ *= 1.17D;
							mc.thePlayer.speedInAir = 0.025f;
							mc.timer.timerSpeed = 1.15f;

							if(mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0)
								setMoveSpeed(getMoveSpeed() + 0.26);
							else
								setMoveSpeed(getMoveSpeed() + 0.02);
						} else {
							if(ticks == 4)
								mc.thePlayer.motionY = 0;

							mc.timer.timerSpeed = 1.09f;

						}
					}
					setMoveSpeed((getMoveSpeed() * 1.006) + (mc.thePlayer.hurtTime > 0 ? damageBoost.getValue() / 5 : 0));
					break;
			case "NCP":
				if(isMovingOnGround()) {
					mc.thePlayer.jump();

					double moveSpeed = 0;

					if(mc.thePlayer.moveForward != 0) {
						if(mc.thePlayer.moveStrafing != 0)
							moveSpeed = 0.45;
						else
							moveSpeed = 0.48;
					} else {
						moveSpeed = 0.4f;
					}
					if (PlayerUtil.hasEffect(Potion.moveSpeed))
						moveSpeed *= 1.0 + 0.17 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

					if(PlayerUtil.hasEffect(Potion.moveSlowdown))
						moveSpeed *= 0.9f;

					setMoveSpeed(moveSpeed);
					mc.thePlayer.motionY -= 0.01f;
					mc.timer.timerSpeed	= 1.13f;
					mc.thePlayer.speedInAir = 0.021f;
				} else {
					setMoveSpeed(getMoveSpeed() * 1.0004);

					if(ticks == 5) {
						mc.thePlayer.motionY = -0.19f;
						mc.timer.timerSpeed = 1.01f;
					}
					if(mc.thePlayer.hurtTime != 0)
						setMoveSpeed(getMoveSpeed() + damageBoost.getValue() / 5);
				}
				break;
			case "Vulcan":
				if(isMovingOnGround()) {
					double moveSpeed = 0;
					mc.thePlayer.jump();
					mc.timer.timerSpeed = 1.15f;
					if(mc.thePlayer.moveForward != 0 && mc.thePlayer.moveStrafing == 0)
						moveSpeed = 0.522;
					else if(mc.thePlayer.moveStrafing != 0)
						moveSpeed = 0.46;
					else
						moveSpeed = getBaseMoveSpeed() * 1.8;

					if (PlayerUtil.hasEffect(Potion.moveSpeed))
						moveSpeed *= 1.0 + 0.09 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

					if(PlayerUtil.hasEffect(Potion.moveSlowdown))
						moveSpeed *= 0.9f;

					setMoveSpeed(moveSpeed);
				} else {
					mc.timer.timerSpeed = 1f;

					if(getMoveSpeed() > 0.332)
						setMoveSpeed(0.331);

					if(ticks == 4)
						mc.thePlayer.motionY = -0.315;

					if(ticks > 3)
						mc.thePlayer.motionY -= 0.0004;

					if(!groundStrafe.isEnabled())
						strafe();
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
				case "Verus Ground":
					if(mc.thePlayer.fallDistance <= 0) {
						if (mc.thePlayer.onGround) {
							if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.ticksExisted % 13 == 0) {
								mc.thePlayer.jump();
								setMoveSpeed(0.5);
							} else
								setMoveSpeed(0.4);
						} else {
							if (!mc.gameSettings.keyBindJump.isKeyDown() && isMoving()) {
								mc.thePlayer.motionY = -0.0980000019;
								setMoveSpeed(0.37);
							}
						}
					}
					strafe();
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
					NotificationManager.addToQueue(new Notification("Lagback", "Disabled Speed for Flagging!", Notification.Type.WARNING));
					setEnabled(false);
				}
			}
		}
	}

}
