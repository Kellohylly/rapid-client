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
	private final Setting mode = new Setting("Mode", this, "Vanilla", "No Ground", "Old NCP", "Vulcan");
	private final Setting delay = new Setting("Delay", this, 100, 0, 200, true);

	private final TimerUtil timer = new TimerUtil();
	private int hits = 0;

	public Criticals() {
		add(mode, delay);
	}

	@Override
	public void settingCheck() {
		delay.setVisible(!mode.getMode().equals("No Ground"));
	}

	@Override
	public void onEnable() {
		if(mode.getMode().equals("No Ground") && mc.thePlayer.onGround)
			mc.thePlayer.jump();

		hits = 0;
	}

	@Override
	public void onDisable() {
		hits = 0;
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
						case "Vulcan":
							hits++;
							if(hits > 7) {
								PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.003, mc.thePlayer.posZ, false));
								PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
								hits = 0;
							}
							break;
						}
					}
				}
			}
		}
	}
}
