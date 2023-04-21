package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.settings.Setting;
import client.rapid.util.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;

@ModuleInfo(getName = "No Slow", getCategory = Category.MOVEMENT)
public class NoSlow extends Module {
	private final Setting
	mode = new Setting("Mode", this, "Vanilla", "Packet"),
	delay = new Setting("Delay", this, 60, 1, 100, true);

	TimerUtil timer = new TimerUtil();

	public NoSlow() {
		add(mode, delay);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			setTag(mode.getMode());

			if (mc.thePlayer.isUsingItem() && KillAura.target == null && isMoving() && !mc.thePlayer.isSneaking()) {
				switch(mode.getMode()) {
				case "Packet":
					if (e.isPre())
						PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));

					if(e.isPost()) {
						if(timer.sleep((int)delay.getValue())) {
							mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999999);
							mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
						}
					}
					break;
	            }
			}
		}
	}
}
