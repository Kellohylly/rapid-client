package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
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
		if(e instanceof EventUpdate && e.isPre()) {
			EventUpdate event = (EventUpdate)e;

			if(isEnabled("Long Jump") && getMode("Long Jump", "Mode").equals("Vulcan"))
				return;

			if(mode.getMode().equals("Vulcan") && mc.thePlayer.fallDistance >= 3) {
				if(mc.thePlayer.ticksExisted % 2 == 0)
						mc.thePlayer.motionY = -0.1476;
					else
						mc.thePlayer.motionY = -0.0975;

				if(mc.thePlayer.ticksExisted % 11 == 0) {
					if(mc.thePlayer.ticksExisted % 5.5 == 0)
						mc.thePlayer.onGround = true;
				}

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
