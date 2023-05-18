package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventRotation;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(getName = "Auto Pot", getCategory = Category.COMBAT)
public class AutoPot extends Module {
    private final Setting mode = new Setting("Mode", this, "Down");
    private final Setting delay = new Setting("Delay", this, 100, 10, 200, true);

    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil downTime = new TimerUtil();

    private boolean hasBadEffect;
    private boolean throwing;

    private int prevSlot;

    public AutoPot() {
        add(delay);
    }

    @Override
    public void onEnable() {
        hasBadEffect = false;
        throwing = false;
        downTime.reset();
    }

    @Override
    public void onDisable() {
        hasBadEffect = false;
        throwing = false;
        downTime.reset();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRotation) {
            EventRotation event = (EventRotation) e;


            if(throwing && !hasBadEffect) {
                switch(mode.getMode()) {
                case "Down":
                    event.setPitch(90);
                    mc.thePlayer.rotationPitchHead = event.getPitch();
                    break;
                }
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            setTag(mode.getMode());

            if(timer.reached((int) delay.getValue() * 10L)) {
                for(int i = 0; i < 9; i++) {
                    ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                    if (stack != null && stack.stackSize != 0 && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata())) {
                        ItemPotion potion = (ItemPotion) stack.getItem();

                        for (PotionEffect effect : potion.getEffects(stack)) {
                            Potion pot = Potion.potionTypes[effect.getPotionID()];

                            if (pot.isBadEffect()) {
                                hasBadEffect = true;
                                break;
                            }
                        }
                        if (!hasBadEffect) {
                            if (!throwing) {
                                prevSlot = mc.thePlayer.inventory.currentItem;
                                throwing = true;
                                downTime.reset();
                            } else {
                                if (downTime.reached(60)) {
                                    this.throwPot(i, stack);
                                    throwing = false;
                                    timer.reset();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void throwPot(int slot, ItemStack stack) {
        mc.thePlayer.inventory.currentItem = slot;

        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);

        mc.thePlayer.inventory.currentItem = prevSlot;
    }

}
