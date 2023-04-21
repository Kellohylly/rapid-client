package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

@ModuleInfo(getName = "Chest Stealer", getCategory = Category.PLAYER)
public class ChestStealer extends Module {
	private final Setting
	delay = new Setting("Delay", this, 100, 10, 500, true),
	autoClose = new Setting("Auto Close", this, true);

	private final TimerUtil timer = new TimerUtil();

	public ChestStealer() {
		add(delay, autoClose);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.currentScreen instanceof GuiChest) {

				GuiChest chest = (GuiChest)mc.currentScreen;

				for(int i = 0; i < chest.inventoryRows * 9; i++) {
					Slot slot = chest.inventorySlots.inventorySlots.get(i);

					if(slot.getStack() != null) {
						if(timer.sleep((int)delay.getValue())) {
							chest.handleMouseClick(slot, slot.slotNumber, 0, 1);
							chest.handleMouseClick(slot, slot.slotNumber, 0, 6);
						}
					}
					if(isEmpty(mc.thePlayer.openContainer) && autoClose.isEnabled())
						mc.thePlayer.closeScreen();
				}
			}
		}
	}
	
    public boolean isEmpty(Container container) {
        for (int i = 0; i < ((container.inventorySlots.size() == 90) ? 54 : 27); ++i) {
            if (container.getSlot(i).getHasStack())
                return false;
        }
        return true;
    }
}
