package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
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
import client.rapid.util.PlayerUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

@ModuleInfo(getName = "Flight", getCategory = Category.MOVEMENT)
public class Flight extends Module {
	private final Setting mode = new Setting("Mode", this, "Creative", "Vanilla", "Old NCP", "Collide", "Verus");
	private final Setting damage = new Setting("Damage", this, "None", "Simple", "Jump", "Wait", "Vulcant");
	private final Setting speed = new Setting("Speed", this, 2, 0.2, 10, false);
	private final Setting fast = new Setting("Fast", this, false);
	private final Setting jump = new Setting("Jump", this, false);
	private final Setting bobbing = new Setting("Bobbing", this, true);

	private double moveSpeed;
	private double startY;
	private boolean canFly;
	private boolean damaged;
	private int ticks;

	private final TimerUtil timer = new TimerUtil();

	public Flight() {
		add(mode, damage, speed, fast, jump, bobbing);
	}

	@Override
	public void settingCheck() {
		speed.setVisible(!mode.getMode().equals("Creative") && !mode.getMode().equals("Collide"));
		fast.setVisible(mode.getMode().equals("Verus") || mode.getMode().equals("Old NCP"));
		bobbing.setVisible(mode.getMode().equals("Old NCP"));
		jump.setVisible(mode.getMode().equals("Collide"));
	}

	@Override
	public void onEnable() {
		startY = mc.thePlayer.posY;
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
		if(e instanceof EventCollide && e.isPre()) {
			EventCollide event = (EventCollide)e;

			switch(mode.getMode()) {
				case "Collide":
					if(jump.isEnabled()) {
						event.setBoundingBox(new AxisAlignedBB(-2, 0, -2, 2, 0, 2).offset(event.getX(), startY, event.getZ()));

						if(mc.gameSettings.keyBindJump.isKeyDown()) {
							event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
						}
					} else {
						event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
					}
					break;
			case "Verus":
				if(mc.thePlayer.isSneaking() || !canFly)
					return;

				if(mc.gameSettings.keyBindJump.isKeyDown() && isMoving())
					event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
				else
					event.setBoundingBox(new AxisAlignedBB(-2, 0, -2, 2, 0, 2).offset(event.getX(), startY, event.getZ()));
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

					// With damage
					if (ticks >= 3 && mc.thePlayer.hurtTime > 1) {
						setMoveSpeed(speed.getValue());
						canFly = true;
						timer.reset();
					} else if (damaged) {
						if(timer.reached(1000)) {
							setMoveSpeed(getBaseMoveSpeed());
						} else {
							setMoveSpeed(speed.getValue());
						}
						if(canFly && timer.reached(575) && mc.thePlayer.onGround) {
							mc.thePlayer.jump();
						}
					}

					// Without damage
					if(mc.thePlayer.onGround) {
						if(canFly && !damaged)
							mc.thePlayer.jump();

						startY = mc.thePlayer.posY;
					}
					break;
				case "Collide":
					if(mc.thePlayer.onGround) {
						if(jump.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())
							mc.thePlayer.jump();

						startY = mc.thePlayer.posY;
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
