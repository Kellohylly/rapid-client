package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.game.EventSettingChange;
import client.rapid.event.events.game.EventWorldLoad;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventMove;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.PacketUtil;
import client.rapid.util.PlayerUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;

@ModuleInfo(getName = "Flight", getCategory = Category.MOVEMENT)
public class Flight extends Module {
	private final Setting mode = new Setting("Mode", this, "Creative", "Vanilla", "Old NCP", "Collide", "Verus", "Position");
	private final Setting damage = new Setting("Damage", this, "None", "Simple", "Jump", "Wait");
	private final Setting speed = new Setting("Speed", this, 2, 0.2, 10, false);
	private final Setting fast = new Setting("Fast", this, false);
	private final Setting autoDisable = new Setting("Auto Disable", this, true);
	private final Setting bobbing = new Setting("Bobbing", this, true);

	private Vec3 vec3;
	private Vector2f vec2f;

	private double moveSpeed, launchY;
	private boolean canFly, damaged;
	private int ticks;

	private double x, z;

	private final TimerUtil timer = new TimerUtil();

	public Flight() {
		add(mode, damage, speed, fast, autoDisable, bobbing);
	}

	@Override
	public void onSettingChange(EventSettingChange e) {
		speed.setVisible(!mode.getMode().equals("Creative") && !mode.getMode().equals("Collide"));
		fast.setVisible(mode.getMode().equals("Verus") || mode.getMode().equals("Old NCP"));
		bobbing.setVisible(mode.getMode().equals("Old NCP"));
	}

	@Override
	public void onEnable() {
		launchY = mc.thePlayer.posY;
		damaged = mc.thePlayer.hurtTime != 0 && !damage.getMode().equals("None");
		moveSpeed = speed.getValue();

		switch(mode.getMode()) {
		case "Old NCP":
			if(mc.thePlayer.onGround) {
				if(damage.getMode().equals("None"))
					mc.thePlayer.jump();

				moveSpeed = speed.getValue();
			} else
				moveSpeed = getBaseMoveSpeed();

			x = mc.thePlayer.posX;
			z = mc.thePlayer.posZ;
			break;
		case "Verus":
			if (mc.thePlayer.onGround) {
				if(damage.getMode().equals("None"))
					mc.thePlayer.jump();

				canFly = true;
			} else {
				NotificationManager.addToQueue(new Notification("Flight", "Verus fly only works on ground", NotificationType.WARNING, 3));
				canFly = false;
			}
			break;
		}
	}

	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		canFly = false;
		damaged = false;
		moveSpeed = 0;
		ticks = 0;
		setMoveSpeed(getMoveSpeed() / 5);
		mc.timer.timerSpeed = 1f;

	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {

			if(bobbing.isEnabled())
				mc.thePlayer.cameraYaw = 0.0696f;
			if (mc.thePlayer.hurtTime != 0)
				damaged = true;

			if(autoDisable.isEnabled() && mc.thePlayer.getHealth() <= 0)
				setEnabled(false);
		}
		if(e instanceof EventWorldLoad && e.isPre() && autoDisable.isEnabled())
			setEnabled(false);

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
						break;
					}
				}
			}
			return;
		}
		if(e instanceof EventCollide && e.isPre()) {
			EventCollide event = (EventCollide)e;

			switch(mode.getMode()) {
			case "Collide":
				event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
				break;
			case "Verus":
				if(mc.thePlayer.isSneaking() || !canFly)
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
					event.setY(mc.thePlayer.ticksExisted % 2 == 0 ? -1.0E-9 : 1.0E-9);
					mc.thePlayer.motionY = 0;

					if(fast.isEnabled()) {
						if (moveSpeed >= getBaseMoveSpeed())
							moveSpeed -= moveSpeed / 102;

						if (mc.thePlayer.isCollidedHorizontally || !isMoving())
							moveSpeed = getBaseMoveSpeed();

						setMoveSpeed(moveSpeed);
					} else
						setMoveSpeed(getBaseMoveSpeed());
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

					moveSpeed = speed.getValue();

					if (mc.gameSettings.keyBindJump.isKeyDown())
						mc.thePlayer.motionY = moveSpeed / 2;

					else if (mc.gameSettings.keyBindSneak.isKeyDown())
						mc.thePlayer.motionY = -moveSpeed / 2;
					else
						mc.thePlayer.motionY = 0;

					setMoveSpeed(moveSpeed);
					break;
				case "Old NCP":
					if (mc.thePlayer.onGround) {
						if (!damaged && mode.getMode().equals("None"))
							return;
						mc.thePlayer.jump();
					}
					break;
				case "Verus":
					mc.thePlayer.capabilities.isFlying = false;

					if (!fast.isEnabled()) {
						if (ticks >= 3 && mc.thePlayer.hurtTime > 1) {
							setMoveSpeed(speed.getValue());
							canFly = true;
							timer.reset();
						} else if (damaged)
							setMoveSpeed(speed.getValue());

						if (canFly && timer.reached(575)) {
							if (mc.thePlayer.onGround) {
								mc.thePlayer.jump();
								launchY = mc.thePlayer.posY;

							}
						}
						if (canFly && timer.reached(1200))
							setMoveSpeed(getBaseMoveSpeed());

					} else {
						if (mc.thePlayer.onGround) {
							canFly = true;

							if (!mc.gameSettings.keyBindJump.isKeyDown() && !isEnabled("Speed") && mc.thePlayer.ticksExisted % 15 == 0) {
								mc.thePlayer.jump();
								setMoveSpeed(0.5);
							} else
								setMoveSpeed(0.35);

							launchY = mc.thePlayer.posY;
						} else {
							if (!mc.gameSettings.keyBindJump.isKeyDown() && canFly && isMoving()) {
								mc.thePlayer.motionY = -0.0980000019;
								setMoveSpeed(0.36);
							}
						}
						setMoveSpeed(getMoveSpeed());
					}
					break;
				case "Position":
					mc.gameSettings.keyBindForward.pressed = false;
					mc.thePlayer.motionY = 0;
					double x = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * speed.getValue();
					double z = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * speed.getValue();

					if(mc.thePlayer.ticksExisted % 2 == 0) {
						PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
						mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
						PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + 30, mc.thePlayer.posZ + z, true));

					}
					break;
			}
		}
		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket)e;

			switch(mode.getMode()) {
				case "Verus":
					if(event.getPacket() instanceof C03PacketPlayer && canFly && !timer.reached(575))
						((C03PacketPlayer) event.getPacket()).setOnGround(true);
					break;
			}
		}
	}
}
