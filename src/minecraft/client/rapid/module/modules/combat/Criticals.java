package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Criticals", getCategory = Category.COMBAT)
public class Criticals extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "No Ground", "Old NCP");
	private final Setting delay = new Setting("Delay", this, 100, 0, 200, true);

	private final TimerUtil timer = new TimerUtil();

	public Criticals() {
		add(mode, delay);
	}

	@Override
	public void onEnable() {
		if(mode.getMode().equals("No Ground") && mc.thePlayer.onGround)
			mc.thePlayer.jump();
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventMotion && e.isPre()) {
			setTag(mode.getMode());

			if(mode.getMode().equals("No Ground") && mc.thePlayer.onGround)
				((EventMotion)e).setGround(false);
		}
		if(e instanceof EventPacket && e.isPre() && !((EventPacket)e).isIncoming()) {
			EventPacket event = (EventPacket)e;

			if(!mode.getMode().equals("No Ground")) {
				if(event.getPacket() instanceof C02PacketUseEntity) {
					C02PacketUseEntity packet = event.getPacket();

					if(packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.onGround && !mc.thePlayer.isInWater() && timer.sleep((int)delay.getValue() * 10L)) {
						switch(mode.getMode()) {
							case "Vanilla":
								double[] vanilla = {0.013D, 0.012D, 0.011D, 0.01D};

								for (double height : vanilla)
									PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ, false));
								break;
							case "Old NCP":
								PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, false));
								PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
								break;
						}
					}
				}
			}
		}
	}
}
