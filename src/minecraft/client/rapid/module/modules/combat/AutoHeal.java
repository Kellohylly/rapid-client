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

@ModuleInfo(getName = "Auto Heal", getCategory = Category.COMBAT)
public class AutoHeal extends Module {
    private final Setting delay = new Setting("Delay", this, 100, 10, 200, true);
    private final Setting health = new Setting("On Health", this, 14, 1, 19, true);
    private final Setting potion = new Setting("Potion", this, true);
    private final Setting gapple = new Setting("Gapple", this, true);

    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil downTime = new TimerUtil();

    private boolean hasBadEffect;
    private boolean throwing;
    private boolean wasEating;

    private int prevSlot;

    public AutoHeal() {
        add(delay, health, potion, gapple);
    }

    @Override
    public void onEnable() {
        hasBadEffect = false;
        throwing = false;
        wasEating = false;
        downTime.reset();
    }

    @Override
    public void onDisable() {
        hasBadEffect = false;
        throwing = false;
        wasEating = false;
        downTime.reset();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRotation && potion.isEnabled()) {
            EventRotation event = (EventRotation) e;

            if(throwing && !hasBadEffect) {
                event.setPitch(90);
                mc.thePlayer.rotationPitchHead = event.getPitch();
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            if(timer.reached((int) delay.getValue() * 10L) && potion.isEnabled()) {
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
                            } else {
                                if (downTime.sleep(80)) {
                                    this.throwPot(i, stack);
                                    timer.reset();
                                }
                            }
                            throwing = true;
                        }
                    }
                }
            }
            if(gapple.isEnabled() && mc.thePlayer.getHealth() <= health.getValue() && !throwing) {
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

    private void throwPot(int slot, ItemStack stack) {
        mc.thePlayer.inventory.currentItem = slot;

        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);

        mc.thePlayer.inventory.currentItem = prevSlot;

        throwing = false;
    }

}
