package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;

@ModuleInfo(getName = "Disabler", getCategory = Category.OTHER)
public class Disabler extends Module {
	private final Setting mode = new Setting("Mode", this, "Transaction", "Payload", "Vulcan Strafe");

	public Disabler() {
		add(mode);
	}

	public void onEnable() {
		if(mode.getMode().equals("Vulcan Strafe"))
			PlayerUtil.addChatMessage(EnumChatFormatting.RED + "Using Digging and Kill Aura on Vulcan will cause AutoBlock flags!");
		else
			PlayerUtil.addChatMessage(EnumChatFormatting.RED + "For some modes you may need to restart!");
	}

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
			}
		}
	}
}
