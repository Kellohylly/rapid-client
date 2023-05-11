package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

@ModuleInfo(getName = "No Fall", getCategory = Category.PLAYER)
public class NoFall extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Ground", "Vulcan", "Verus");
	private final Setting distance = new Setting("Fall Distance", this, 4, 2, 4, false);

	public NoFall() {
		add(mode, distance);
	}

	@Override
	public void onEvent(Event e) {
		if(isEnabled("Anti Fall") && !PlayerUtil.isBlockUnder())
			return;

		if(e instanceof EventCollide && e.isPre() && mode.getMode().equals("Verus")) {
			EventCollide event = (EventCollide)e;

			if(mc.thePlayer.fallDistance >= distance.getValue()) {
				event.setBoundingBox(new AxisAlignedBB(-5, -1, -5, 5, 1, 5).offset(event.getX(), event.getY(), event.getZ()));
			}
		}
		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket)e;
			switch(mode.getMode()) {
			case "Vulcan":
		        if(event.getPacket() instanceof C03PacketPlayer && !event.isIncoming()) {
		        	C03PacketPlayer packet = event.getPacket();

					if(isEnabled("Long Jump") && getMode("Long Jump", "Mode").equals("Vulcan"))
						return;

					if(mc.thePlayer.fallDistance >= distance.getValue()) {
						if(!mc.thePlayer.onGround) {
							mc.thePlayer.fallDistance = -0.1f;
							mc.thePlayer.motionY = -0.1;
							setMoveSpeed(0.3);
						}
						packet.setOnGround(!packet.isOnGround());
					}
		        }
				break;
			}
		}
		if(e instanceof EventMotion && e.isPre()) {
			setTag(mode.getMode());

			if(mc.thePlayer.fallDistance >= distance.getValue()) {
				switch(mode.getMode()) {
				case "Vanilla":
					PacketUtil.sendPacketSilent(new C03PacketPlayer(true));
					break;
				case "Ground":
					((EventMotion)e).setGround(true);
					break;
				}
			}
		}
	}
}
