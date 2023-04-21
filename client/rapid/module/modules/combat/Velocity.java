package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.*;

@ModuleInfo(getName = "Velocity", getCategory = Category.COMBAT)
public class Velocity extends Module {
	private final Setting
	horizontal = new Setting("Horizontal", this, 50, 0, 100, true),
	vertical = new Setting("Vertical", this, 50, 0, 100, true),
	explosives = new Setting("Explosives", this, true),
	transaction = new Setting("Transaction", this, true),
	zeroInAir = new Setting("Zero in Air", this, false);

	public Velocity() {
		add(horizontal, vertical, explosives, transaction, zeroInAir);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventPacket && e.isPre()) {

			setTag((int)horizontal.getValue() + "% " + (int)vertical.getValue() + "%");

			EventPacket event = (EventPacket)e;

			if(transaction.isEnabled() && event.getPacket() instanceof C0FPacketConfirmTransaction) {
				C0FPacketConfirmTransaction transaction = event.getPacket();

				if (transaction.getUid() >= -31767 && transaction.getUid() <= -30769)
					event.cancel();
			}
			
			if(event.getPacket() instanceof S12PacketEntityVelocity) {
				S12PacketEntityVelocity packet = event.getPacket();
				if(zeroInAir.isEnabled() && !mc.thePlayer.onGround) {
					event.cancel();
					return;
				}

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
				if(zeroInAir.isEnabled() && !mc.thePlayer.onGround) {
					event.cancel();
					return;
				}
				if (horizontal.getValue() != 0 || vertical.getValue() != 0) {

					packet.field_149152_f *= horizontal.getValue() / 100;
					packet.field_149153_g *= vertical.getValue() / 100;
					packet.field_149159_h *= horizontal.getValue() / 100;
				} else
					event.cancel();
			}
		}
	}
}
