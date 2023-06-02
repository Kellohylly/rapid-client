package client.rapid.module.modules.player;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Fast Eat", getCategory = Category.PLAYER)
public class FastEat extends Module {
	private final Setting packets = new Setting("Packets", this, 20, 10, 50, true);
	private final Setting startTime = new Setting("Start Time", this, 14, 1, 19, true);
	private final Setting onStop = new Setting("Do On Stop", this, true);

	public FastEat() {
		add(packets, startTime, onStop);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.isEating() && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood && mc.thePlayer.getHealth() > 0 && mc.thePlayer.getItemInUseDuration() > startTime.getValue()) {
			for(int i = 0; i < packets.getValue(); i++)
				PacketUtil.sendPacketSilent(new C03PacketPlayer());

			if(onStop.isEnabled())
				mc.playerController.onStoppedUsingItem(mc.thePlayer);
		}
	}
}
