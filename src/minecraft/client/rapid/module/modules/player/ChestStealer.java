package client.rapid.module.modules.player;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.ItemUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(getName = "Chest Stealer", getCategory = Category.PLAYER)
public class ChestStealer extends Module {
	private final Setting delay = new Setting("Delay", this, 100, 10, 500, true);
	private final Setting titleCheck = new Setting("Title Check", this, true);
	private final Setting ignoreBadItems = new Setting("Ignore Bad Items", this, true);
	private final Setting autoClose = new Setting("Auto Close", this, true);

	private final TimerUtil timer = new TimerUtil();

	public ChestStealer() {
		add(delay, titleCheck, ignoreBadItems, autoClose);
	}

	@Override
	public void onEnable() {
		timer.reset();
	}

	@Override
	public void onDisable() {
		timer.reset();
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.currentScreen instanceof GuiChest) {

				GuiChest chest = (GuiChest) mc.currentScreen;

				if(titleCheck.isEnabled() && !isChest(chest) && !chest.stealing)
					return;

				for(int i = 0; i < chest.inventoryRows * 9; i++) {
					Slot slot = chest.inventorySlots.inventorySlots.get(i);

					if(slot.getStack() != null) {
						if(ignoreBadItems.isEnabled() && ItemUtil.isBadItem(slot.getStack().getItem())) {
							continue;
						}
						if(timer.sleep((int)delay.getValue())) {
							chest.handleMouseClick(slot, slot.slotNumber, 0, 1);
							chest.handleMouseClick(slot, slot.slotNumber, 0, 6);
						}
					}
					if(isEmpty(mc.thePlayer.openContainer) && autoClose.isEnabled() && !chest.stealing && timer.reached((int)delay.getValue()))
						mc.thePlayer.closeScreen();
				}
			}
		}
	}

	private boolean isChest(GuiChest chest) {
		String title = chest.lowerChestInventory.getDisplayName().getUnformattedText();

		return title.contains("Chest");
	}
	
    public static boolean isEmpty(Container container) {
        for (int i = 0; i < ((container.inventorySlots.size() == 90) ? 54 : 27); ++i) {
            if (container.getSlot(i).getHasStack()) {
				return Wrapper.getSettingsManager().getSettingByName("Chest Stealer", "Ignore Bad Items").isEnabled() && ItemUtil.isBadItem(container.inventorySlots.get(i).getStack().getItem());
			}
        }
        return true;
    }
}
