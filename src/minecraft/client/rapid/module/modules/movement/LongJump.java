package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Long Jump", getCategory = Category.MOVEMENT)
public class LongJump extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Old NCP", "NCP", "Vulcan");
	private final Setting damage = new Setting("Damage", this, "None", "Simple", "Jump", "Wait");
	private final Setting speed = new Setting("Speed", this, 1.6, 0.2, 10, false);
	private final Setting height = new Setting("Height", this, 1.6, 0.2, 10, false);
	private final Setting slowdown = new Setting("Slowdown Speed", this, 0.02, 0.01, 0.2, false);
	private final Setting autoDisable = new Setting("Auto Disable", this, false);
	
	private double moveSpeed;
	private boolean jumped, canJump, damaged;
	private int ticks;

	public LongJump() {
		add(mode, damage, speed, height, slowdown, autoDisable);
	}

	@Override
	public void onEnable() {
		if(mode.getMode().equals("Vulcan"))
			moveSpeed = 0;
		else
			moveSpeed = speed.getValue();

		damaged = mc.thePlayer.hurtTime != 0;
		canJump = false;
		ticks = 0;
	}

	@Override
	public void onDisable() {
		jumped = false;
		moveSpeed = 0;
		mc.timer.timerSpeed = 1f;
		canJump = false;
		mc.thePlayer.speedInAir = 0.02f;
		ticks = 0;
		damaged = false;
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.thePlayer.hurtTime != 0)
				damaged = true;
		}
		if(!damaged && !damage.getMode().equals("None")) {
			if(e instanceof EventUpdate && e.isPre()) {
				setMoveSpeed(0);
				switch(damage.getMode()) {
					case "Simple":
						PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.001, mc.thePlayer.posZ, false));
						PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
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
		if(e instanceof EventPacket && mode.getMode().equals("Vulcan") && mc.thePlayer.fallDistance >= 4 && ((EventPacket) e).getPacket() instanceof C03PacketPlayer && !isEnabled("No Fall")) {
			C03PacketPlayer packet = ((EventPacket) e).getPacket();
			mc.thePlayer.fallDistance = 0;
			mc.thePlayer.motionY = -0.1;
			packet.setOnGround(!packet.isOnGround());

		}
		if(e instanceof EventUpdate && e.isPre()) {
			switch(mode.getMode()) {
			case "Vanilla":
				if(mc.thePlayer.onGround) {
					setMoveSpeed(0);
					if(jumped && autoDisable.isEnabled())
						setEnabled(false);
					else {
						mc.thePlayer.jump();
						mc.thePlayer.motionY *= height.getValue();
					}
				} else {
					jumped = true;
					this.setMoveSpeed(speed.getValue());
				}
				break;
			case "Old NCP":
				if(mc.thePlayer.onGround) {
					if(jumped && autoDisable.isEnabled()) {
						setMoveSpeed(0);
						setEnabled(false);
					} else {
						if(!autoDisable.isEnabled())
							moveSpeed = speed.getValue();

						mc.thePlayer.jump();
					}
				} else {
					jumped = true;
					if(moveSpeed > getBaseMoveSpeed())
						moveSpeed -= slowdown.getValue() / 10;

					this.setMoveSpeed(moveSpeed);
				}
				break;
				case "NCP":
					if(!canJump) {
						mc.gameSettings.keyBindForward.pressed = true;
						mc.gameSettings.keyBindJump.pressed = false;

						if(!mc.thePlayer.isUsingItem())
							setMoveSpeed(-0.1275);
						else
							setMoveSpeed(-0.02);

						mc.thePlayer.moveStrafing *= 0;
						mc.thePlayer.moveForward *= 0;

						if(mc.thePlayer.hurtTime > 0)
							canJump = true;
						return;
					}
					if(mc.thePlayer.onGround) {
						if(canJump && !jumped) {
							mc.thePlayer.jump();
							mc.thePlayer.motionY *= 1.04f;
							mc.thePlayer.speedInAir = 0.022f;
							moveSpeed = 0.69;
							mc.timer.timerSpeed = 0.85f;
						} else if(canJump) {
							mc.timer.timerSpeed = 1f;
							setEnabled(false);
						}
					} else {
						moveSpeed = getMoveSpeed();
						jumped = true;

						if(moveSpeed <= 0.52 && !(mc.thePlayer.fallDistance > 0))
							moveSpeed = 0.52;

					}
					setMoveSpeed(moveSpeed);
					break;
				case "Vulcan":
					if(mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0) {
						if(jumped && autoDisable.isEnabled())
							setEnabled(false);
						else {
							moveSpeed = 0;
							mc.thePlayer.jump();
						}
					}
					if(mc.thePlayer.fallDistance > 0) {
						jumped = true;
						if (mc.thePlayer.fallDistance >= 0.1 && moveSpeed < 3) {
							mc.thePlayer.sendChatMessage(".vclip " + height.getValue());
							moveSpeed += 1;
						}

						if(!mc.thePlayer.onGround) {
							if (mc.thePlayer.ticksExisted % 2 == 0)
								mc.thePlayer.motionY = -0.1476;
							else {
								mc.thePlayer.motionY = -0.0975;

							}
						}
						if(isEnabled("Disabler") && getMode("Disabler", "Mode").equals("Vulcan Strafe"))
							setMoveSpeed(getBaseMoveSpeed() + 0.0449);
					}
					break;
			}
		}
	}
}