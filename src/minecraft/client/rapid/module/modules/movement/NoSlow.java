package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventSlowdown;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(getName = "No Slow", getCategory = Category.MOVEMENT)
public class NoSlow extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Packet", "NCP");
	private final Setting delay = new Setting("Delay", this, 60, 1, 100, true);
	private final Setting allowSprinting = new Setting("Allow Sprinting", this, true);

	private final TimerUtil timer = new TimerUtil();

	public NoSlow() {
		add(mode, delay, allowSprinting);
	}

	@Override
	public void updateSettings() {
		delay.setVisible(!mode.getMode().equals("Vanilla"));
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		if(e instanceof EventSlowdown) {
			EventSlowdown event = (EventSlowdown) e;

			if (mc.thePlayer.isUsingItem() && MoveUtil.isMoving() && !mc.thePlayer.isSneaking()) {
				if(mc.thePlayer.isSprinting() && !allowSprinting.isEnabled())
					mc.thePlayer.setSprinting(false);

				switch(mode.getMode()) {
					case "Vanilla":
						event.cancel();
						break;
					case "Packet":
						event.cancel();

						if(mc.thePlayer.getHeldItem() != null && KillAura.target == null && mc.thePlayer.isUsingItem()) {
							if(mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
								return;
							}
							if (e.isPost())
								PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));

							if (e.isPre() && timer.sleep((int) delay.getValue())) {
								mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999999);
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
							}
						}
						break;
					case "NCP":
						if(mc.thePlayer.getHeldItem() != null && KillAura.target == null) {
							if(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
								if(mc.thePlayer.onGround)
									event.cancel();

								if (e.isPost() && timer.sleep((int)delay.getValue()))
									PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

								if (e.isPre()) {
									PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
									PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
								}
							}
						}
						if(KillAura.target != null) {
							event.cancel();
						}
						break;
				}
			}
		}
	}
}
