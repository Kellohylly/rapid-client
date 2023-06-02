package client.rapid.module.modules.combat;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
* Modified Version from MintyCodes AutoArmor
 */
@ModuleInfo(getName = "Auto Armor", getCategory = Category.COMBAT)
public class AutoArmor extends Module {
    private final Setting delay = new Setting("Delay", this, 20, 1, 600, true);
    private final Setting inventoryOnly = new Setting("Inventory Only", this, false);

    private final int[] helmet = {310, 306, 314, 302, 298};
    private final int[] chestplate = {311, 307, 315, 303, 299};
    private final int[] leggings = {312, 308, 316, 304, 300};
    private final int[] boots = {313, 309, 317, 305, 301};

    private boolean best = true;
    public static boolean selecting = true;

    private final TimerUtil timer = new TimerUtil();

    public AutoArmor() {
        add(delay, inventoryOnly);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(inventoryOnly.isEnabled() && !(mc.currentScreen instanceof GuiInventory))
                return;

            if(timer.sleep((int) delay.getValue())) {
                autoArmor();
                betterArmor();
            }
        }
    }

    public void autoArmor() {
        if(!best) {
            int item = -1;

            if (mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                int length = (boots = this.boots).length;
                for (int i = 0; i < length; i++) {
                    int id = boots[i];
                    if (getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
                if (mc.thePlayer.inventory.armorInventory[1] == null) {
                    int[] leggings;
                    int length2 = (leggings = this.leggings).length;
                    for (int i = 0; i < length2; i++) {
                        int id = leggings[i];
                        if (getItem(id) != -1) {
                            item = getItem(id);
                            break;
                        }
                    }
                }
                if (mc.thePlayer.inventory.armorInventory[2] == null) {
                    int[] chestplate;
                    int length3 = (chestplate = this.chestplate).length;
                    for (int i = 0; i < length3; i++) {
                        int id = chestplate[i];
                        if (getItem(id) != -1) {
                            item = getItem(id);
                            break;
                        }
                    }
                }
                if (mc.thePlayer.inventory.armorInventory[3] == null) {
                    int[] helmet;
                    int length4 = (helmet = this.helmet).length;
                    for (int i = 0; i < length4; i++) {
                        int id = helmet[i];
                        if (getItem(id) != -1) {
                            item = getItem(id);
                            break;
                        }
                    }
                }
                if (item != -1) {
                    selecting = true;
                    mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
                } else
                    selecting = false;
            }
        }
    }

    public void betterArmor() {
        if(best) {
        if((mc.thePlayer.openContainer == null || mc.thePlayer.openContainer.windowId == 0)) {
            boolean switchArmor = false;
            int item = -1;
            int[] array;
            int i;
            if (mc.thePlayer.inventory.armorInventory[0] == null) {
                int j = (array = this.boots).length;
                for (i = 0; i < j; i++) {
                    int id = array[i];
                    if (getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            if (mc.thePlayer.inventory.armorInventory[3] == null) {
                int j = (array = this.helmet).length;
                for (i = 0; i < j; i++) {
                    int id = array[i];
                    if (getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            if (mc.thePlayer.inventory.armorInventory[1] == null) {
                int j = (array = this.leggings).length;
                for (i = 0; i < j; i++) {
                    int id = array[i];
                    if (getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            if (mc.thePlayer.inventory.armorInventory[2] == null) {
                int j = (array = this.chestplate).length;
                for (i = 0; i < j; i++) {
                    int id = array[i];
                    if (getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            ItemStack[] stackArray;
            int k = (stackArray = mc.thePlayer.inventory.mainInventory).length;
            for (int j = 0; j < k; j++) {
                ItemStack stack = stackArray[j];
                if (stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = switchArmor && !b;
            if (item != -1) {
                selecting = true;
                mc.playerController.windowClick(0, item, 0, switchArmor ? 4 : 1, mc.thePlayer);
            } else
                selecting = false;
        }
        }
    }

    public boolean isBetterArmor(int slot, int[] armorType) {
        if(mc.thePlayer.inventory.armorInventory[slot] != null) {
            int currentIndex = 0;
            int invIndex = 0;
            int finalCurrentIndex = -1;
            int finalInvIndex = -1;
            int[] array;
            int j = (array = armorType).length;
            for(int i = 0; i < j; i++) {
                int armor = array[i];
                if(Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
                    finalCurrentIndex = currentIndex;
                    break;
                }
                currentIndex++;
            }
            j = (array = armorType).length;
            for(int i = 0; i < j; i++) {
                int armor = array[i];
                if(getItem(armor) != -1) {
                    finalInvIndex = invIndex;
                    break;
                }
                invIndex++;
            }
            if(finalInvIndex > -1)
                return finalInvIndex < finalCurrentIndex;
        }
        return false;
    }

    public int getItem(int id) {
        for(int i = 9; i < 45; i++) {
            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if(item != null && Item.getIdFromItem(item.getItem()) == id)
                return i;
        }
        return -1;
    }
}
