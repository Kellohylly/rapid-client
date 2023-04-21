package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Long Jump", getCategory = Category.MOVEMENT)
public class LongJump extends Module {
	private final Setting
	mode = new Setting("Mode", this, "Vanilla", "NCP", "Vulcan"),
	speed = new Setting("Speed", this, 1.6, 0.2, 10, false),
	height = new Setting("Height", this, 1.6, 0.2, 10, false),
	slowdown = new Setting("Slowdown Speed", this, 0.02, 0.01, 0.2, false),
	autoDisable = new Setting("Auto Disable", this, false);
	
	private double moveSpeed;
	private boolean jumped;

	public LongJump() {
		add(mode, speed, height, slowdown, autoDisable);
	}
	
	public void onEnable() {
		if(mode.getMode().equals("Vulcan"))
			moveSpeed = 0;
		else
			moveSpeed = speed.getValue();
	}
	
	public void onDisable() {
		jumped = false;
		moveSpeed = 0;
		mc.timer.timerSpeed = 1f;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventPacket && mode.getMode().equals("Vulcan") && mc.thePlayer.fallDistance >= 4 && ((EventPacket) e).getPacket() instanceof C03PacketPlayer && !isEnabled("No Fall")) {
			C03PacketPlayer packet = ((EventPacket) e).getPacket();
			mc.thePlayer.fallDistance = 0;
			mc.thePlayer.motionY = -0.1;
			packet.setOnGround(!packet.isOnGround());

		}
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());

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
			case "NCP":
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