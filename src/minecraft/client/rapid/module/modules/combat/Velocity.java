package client.rapid.module.modules.combat;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.*;

@ModuleInfo(getName = "Velocity", getCategory = Category.COMBAT)
public class Velocity extends Module {
	private final Setting mode = new Setting("Mode", this, "Cancel", "Vulcan", "Ground Only", "Ticks");
	private final Setting ticks = new Setting("Ticks", this, 2, 1, 5, true);
	private final Setting horizontal = new Setting("Horizontal", this, 50, 0, 100, true);
	private final Setting vertical = new Setting("Vertical", this, 50, 0, 100, true);
	private final Setting explosives = new Setting("Explosives", this, true);

	public Velocity() {
		add(mode, ticks, horizontal, vertical, explosives);
	}

	private int tickss;

	@Override
	public void updateSettings() {
		ticks.setVisible(mode.getMode().equals("Ticks"));
	}

	@Override
	public void onEnable() {
		tickss = 0;
	}

	@Override
	public void onDisable() {
		tickss = 0;
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		if(e instanceof EventUpdate && e.isPre() && mode.getMode().equals("Ticks")) {
			if(mc.thePlayer.hurtTime != 0)
				tickss++;
			else
				tickss = 0;

			if(tickss == ticks.getValue()) {
				mc.thePlayer.motionY *= vertical.getValue() / 100;
				mc.thePlayer.motionX *= horizontal.getValue() / 100;
				mc.thePlayer.motionZ *= horizontal.getValue() / 100;
			}
		}

		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket)e;

			switch(mode.getMode()) {
			case "Cancel":
				doVelocity(event);
				break;
			case "Vulcan":
				doVelocity(event);

				if(event.getPacket() instanceof C0FPacketConfirmTransaction) {
					C0FPacketConfirmTransaction transaction = event.getPacket();

					if (transaction.getUid() >= -31767 && transaction.getUid() <= -30769) {
						event.cancel();
					}
				}
				break;
			case "Ground Only":
				if(mc.thePlayer.onGround)
					doVelocity(event);
				else {
					if ((event.getPacket() instanceof S12PacketEntityVelocity) || (event.getPacket() instanceof S27PacketExplosion && explosives.isEnabled()))
						event.cancel();
				}
				break;
			}
		}
	}

	private void doVelocity(EventPacket event) {
		if(event.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity packet = event.getPacket();

			if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
				if (horizontal.getValue() != 0 || vertical.getValue() != 0) {
					packet.motionX *= horizontal.getValue() / 100;
					packet.motionY *= vertical.getValue() / 100;
					packet.motionZ *= horizontal.getValue() / 100;
				} else
					event.cancel();
			}

		}

		if(event.getPacket() instanceof S27PacketExplosion && explosives.isEnabled()) {
			S27PacketExplosion packet = event.getPacket();
			if (horizontal.getValue() != 0 || vertical.getValue() != 0) {
				packet.field_149152_f *= horizontal.getValue() / 100;
				packet.field_149153_g *= vertical.getValue() / 100;
				packet.field_149159_h *= horizontal.getValue() / 100;
			} else
				event.cancel();
		}
	}

}
