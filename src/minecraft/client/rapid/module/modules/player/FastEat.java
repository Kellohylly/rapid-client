package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.PacketUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Fast Eat", getCategory = Category.PLAYER)
public class FastEat extends Module {

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.isEating() && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood && mc.thePlayer.getHealth() > 0) {
			for(int i = 0; i < 35; i++)
				PacketUtil.sendPacketSilent(new C03PacketPlayer());
		}
	}
}
