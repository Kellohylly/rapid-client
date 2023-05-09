package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(getName = "Disabler", getCategory = Category.OTHER)
public class Disabler extends Module {
	private final Setting mode = new Setting("Mode", this, "Transaction", "Payload", "Vulcan Strafe", "Timer");
	
	public Disabler() {
		add(mode);
	}

	@Override
	public void onEnable() {
		if(mode.getMode().equals("Vulcan Strafe"))
			NotificationManager.addToQueue(new Notification("Disabler", "This can flag for Auto Block when attacking!", Notification.Type.WARNING));
		else
			NotificationManager.addToQueue(new Notification("Note", "Some disablers may require a restart.", Notification.Type.INFO));
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre() && mode.getMode().equals("Vulcan Strafe") && mc.thePlayer.ticksExisted % 4 == 0) {
			PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), EnumFacing.DOWN));

		}
		if(e instanceof EventPacket && e.isPre()) {
			setTag(mode.getMode());

			EventPacket event = (EventPacket)e;

			switch(mode.getMode()) {
				case "Transaction":
					if(event.getPacket() instanceof C0FPacketConfirmTransaction)
						e.cancel();
				break;
			case "Payload":
				if(event.getPacket() instanceof C17PacketCustomPayload)
					e.cancel();
				break;
				case "Timer":
					if(event.getPacket() instanceof C03PacketPlayer && mc.thePlayer.ticksExisted % 3 == 0)
						event.cancel();
					break;
			}
		}
	}
}
