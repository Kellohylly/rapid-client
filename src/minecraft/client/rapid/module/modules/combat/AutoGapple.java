package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

@ModuleInfo(getName = "Auto Gapple", getCategory = Category.COMBAT)
public class AutoGapple extends Module {
    private final Setting health = new Setting("On Health", this, 14, 1, 19, true);
    private int prevSlot;
    private boolean wasEating;

    public AutoGapple() {
        add(health);
    }

    @Override
    public void onEnable() {
        wasEating = false;
    }

    @Override
    public void onDisable() {
        wasEating = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if((mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemAppleGold) || mc.thePlayer.getHeldItem() == null) && !wasEating && !mc.thePlayer.isUsingItem()) {
                prevSlot = mc.thePlayer.inventory.currentItem;
            }
            if(mc.thePlayer.getHealth() <= health.getValue()) {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                    if (stack != null && stack.stackSize != 0 && stack.getItem() instanceof ItemAppleGold) {
                        wasEating = true;
                        mc.thePlayer.inventory.currentItem = i;
                        mc.gameSettings.keyBindUseItem.pressed = true;

                    }
                }
            } else {
                if(wasEating) {
                    mc.thePlayer.inventory.currentItem = prevSlot;
                    mc.gameSettings.keyBindUseItem.pressed = false;
                    wasEating = false;
                }
            }
        }
    }
}
