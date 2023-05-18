package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

@ModuleInfo(getName = "Auto Heal", getCategory = Category.COMBAT)
public class AutoHeal extends Module {
    private final Setting delay = new Setting("Delay", this, 100, 10, 1000, true);
    private final Setting health = new Setting("On Health", this, 14, 1, 20, true);

    private final TimerUtil timer = new TimerUtil();

    private boolean wasEating;
    private boolean canEat;

    private int prevSlot;

    public AutoHeal() {
        add(delay, health);
    }

    @Override
    public void onEnable() {
        canEat = true;
        wasEating = false;
        timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        canEat = true;
        wasEating = false;
        timer.reset();
        super.onDisable();
    }

    @Override
    public void onEvent(Event e) {
        if(timer.reached((int)delay.getValue() * 10L) && !canEat)
            canEat = true;

        if(e instanceof EventUpdate && e.isPre() && canEat) {
            if(mc.thePlayer.getHealth() <= health.getValue()) {
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
                        timer.reset();

                        mc.thePlayer.inventory.currentItem = prevSlot;
                        mc.gameSettings.keyBindUseItem.pressed = false;
                        wasEating = false;
                        canEat = false;
                    }
                }
            }
            if(wasEating && timer.time() <= 100 && mc.thePlayer.inventory.currentItem != prevSlot) {
                mc.thePlayer.inventory.currentItem = prevSlot;
                mc.gameSettings.keyBindUseItem.pressed = false;
                wasEating = false;
                canEat = false;
            }
        }
    }
}
