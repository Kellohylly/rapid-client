package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventSettingChange;
import client.rapid.event.events.player.EventStep;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Step", getCategory = Category.MOVEMENT)
public class Step extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Matrix", "Ground", "Packet", "Motion");
	private final Setting height = new Setting("Height", this, 1.5, 1, 2, false);
	private final Setting delay = new Setting("Delay", this, 0, 0, 600, true);

	private final TimerUtil timer = new TimerUtil();

	private boolean stepped;

	public Step() {
		add(mode, height, delay);
	}

	@Override
	public void onSettingChange(EventSettingChange e) {
		height.setVisible(mode.getMode().equals("Vanilla") || mode.getMode().equals("Packet"));
	}

	@Override
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.6F;
		stepped = false;
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		if(e instanceof EventStep) {
			EventStep event = (EventStep)e;

			double rheight = mc.thePlayer.getEntityBoundingBox().minY + 1 - mc.thePlayer.posY;

			if(timer.reached((int)delay.getValue())) {
				if (mc.thePlayer.isCollidedHorizontally) {
					switch(mode.getMode()) {
						case "Vanilla":
							event.setHeight((float) height.getValue());
							break;
						case "Packet":
							event.setHeight(1f);
							break;
					}
				} else
					event.setHeight(0.6f);

				if (rheight >= 0.8) {
					switch(mode.getMode()) {
						case "Packet":
						if (rheight <= 1) {
							PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4199, mc.thePlayer.posZ, false));
							PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7532, mc.thePlayer.posZ, false));
							timer.reset();
						}
						break;
					}
				}
			}
		}

		if(e instanceof EventUpdate && e.isPre() && timer.reached((int)delay.getValue())) {
			switch (mode.getMode()) {
			case "Matrix":
			case "Motion":
				if (mc.thePlayer.isCollidedHorizontally && isMovingOnGround()) {
					mc.thePlayer.jump();
					stepped = true;
				} else {
					if(mc.thePlayer.isCollidedHorizontally && stepped && isMoving()) {

					}
					if (!mc.thePlayer.isCollidedHorizontally && stepped && isMoving()) {
						mc.thePlayer.motionY = 0;
						stepped = false;

						if (mode.getMode().equals("Motion"))
							setMoveSpeed(0.2);
						else
							setMoveSpeed(0.06);
						timer.reset();

					}
				}
				break;
				case "Ground":
					if (mc.thePlayer.isCollidedHorizontally && isMoving() && mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						stepped = true;
					} else {
						if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.onGround && stepped) {
							mc.thePlayer.onGround = true;
							stepped = false;
							timer.reset();
						}
					}
					break;
			}
		}
	}
}
