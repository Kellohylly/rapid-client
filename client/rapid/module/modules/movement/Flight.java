package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.*;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.*;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.*;

@ModuleInfo(getName = "Flight", getCategory = Category.MOVEMENT)
public class Flight extends Module {
	private final Setting
	mode = new Setting("Mode", this, "Creative", "Vanilla", "Old NCP", "Collide", "Verus"),
	damage = new Setting("Damage", this, false),
	speed = new Setting("Speed", this, 12, 1, 100, false),
	slowdown = new Setting("Slowdown", this, 0.2, 0, 1, false);

	private double moveSpeed, launchY;
	private boolean damaged;

	public Flight() {
		add(mode, speed, slowdown, damage);
	}

	public void onEnable() {
		launchY = mc.thePlayer.posY;

		switch(mode.getMode()) {
		case "Old NCP":
			if(mc.thePlayer.onGround) {
				mc.thePlayer.jump();

				moveSpeed = speed.getValue() / 10;
			} else
				moveSpeed = getBaseMoveSpeed();

			break;
		case "Verus":
			if(mc.thePlayer.onGround) {
				if(damage.isEnabled()) {
					PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.001, mc.thePlayer.posZ, false));
					PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
				}
				damaged = true;
			} else {
				PlayerUtil.addChatMessage(EnumChatFormatting.RED + "Verus Flight only works on Ground!");
				damaged = false;
			}
			break;
		}
	}

	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		moveSpeed = 0;

		if(damaged)
			setMoveSpeed(0);

		damaged = false;

	}
	
	public void onEvent(Event e) {
		if(e instanceof EventCollide && e.isPre()) {
			EventCollide event = (EventCollide)e;

			switch(mode.getMode()) {
			case "Collide":
				event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
				break;
			case "Verus":
				if(mc.thePlayer.isSneaking() || !damaged && !damage.isEnabled())
					return;

				if(mc.gameSettings.keyBindJump.isKeyDown() && isMoving())
					event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
				else
					event.setBoundingBox(new AxisAlignedBB(-2, 0, -2, 2, 0, 2).offset(event.getX(), launchY, event.getZ()));
				break;
			}
		}
		if(e instanceof EventMove && e.isPre()) {
			EventMove event = (EventMove)e;
			
			switch(mode.getMode()) {
			case "Old NCP":
				mc.thePlayer.capabilities.isFlying = false;

				if(!mc.thePlayer.onGround) {
					if (mc.thePlayer.ticksExisted % 2 == 0)
						event.setY(-1.0E-9);
					else
						event.setY(1.0E-9);

					mc.thePlayer.motionY = 0;

					if(moveSpeed >= getBaseMoveSpeed())
						moveSpeed -= slowdown.getValue() / 10;
					
					if(mc.thePlayer.isCollidedHorizontally || !isMoving())
						moveSpeed = getBaseMoveSpeed();

					setMoveSpeed(moveSpeed);
				}
				break;
			}
		}
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());

			switch(mode.getMode()) {
			case "Creative":
				mc.thePlayer.capabilities.isFlying = true;
				break;
			case "Vanilla":
				mc.thePlayer.capabilities.isFlying = false;

				moveSpeed = speed.getValue() / 10;

				if(mc.gameSettings.keyBindJump.isKeyDown())
					mc.thePlayer.motionY = moveSpeed / 2;

				else if(mc.gameSettings.keyBindSneak.isKeyDown())
					mc.thePlayer.motionY = -moveSpeed / 2;
				else
					mc.thePlayer.motionY = 0;
				
				setMoveSpeed(moveSpeed);
				break;
			case "Verus":
				mc.thePlayer.capabilities.isFlying = false;

				if(mc.thePlayer.onGround) {
					if(!damaged && !damage.isEnabled())
						damaged = true;

					if(!mc.gameSettings.keyBindJump.isKeyDown() && !isEnabled("Speed"))
							mc.thePlayer.jump();

					launchY = mc.thePlayer.posY;
				}
				if(damaged && damage.isEnabled())
					setMoveSpeed(speed.getValue() / 10);

				if(mc.thePlayer.hurtTime > 8)
					damaged = false;

				setMoveSpeed(getMoveSpeed());
				break;
			}
		}
	}
}
