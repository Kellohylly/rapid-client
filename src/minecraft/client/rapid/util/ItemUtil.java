package client.rapid.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGlassBottle;

public class ItemUtil {

    public static boolean isBadItem(Item item) {
        return
            item.getUnlocalizedName().contains("tnt") ||
            item.getUnlocalizedName().contains("stick") ||
            item.getUnlocalizedName().contains("egg") ||
            item.getUnlocalizedName().contains("string") ||
            item.getUnlocalizedName().contains("flint") ||
            item.getUnlocalizedName().contains("bow") ||
            item.getUnlocalizedName().contains("arrow") ||
            item.getUnlocalizedName().contains("bucket") ||
            item.getUnlocalizedName().contains("feather") ||
            item.getUnlocalizedName().contains("snow") ||
            item.getUnlocalizedName().contains("piston") ||
            item instanceof ItemGlassBottle ||
            item.getUnlocalizedName().contains("web") ||
            item.getUnlocalizedName().contains("slime") ||
            item.getUnlocalizedName().contains("trip") ||
            item.getUnlocalizedName().contains("wire") ||
            item.getUnlocalizedName().contains("sugar") ||
            item.getUnlocalizedName().contains("note") ||
            item.getUnlocalizedName().contains("record") ||
            item.getUnlocalizedName().contains("flower") ||
            item.getUnlocalizedName().contains("wheat") ||
            item.getUnlocalizedName().contains("fishing") ||
            item.getUnlocalizedName().contains("boat") ||
            item.getUnlocalizedName().contains("leather") ||
            item.getUnlocalizedName().contains("seeds") ||
            item.getUnlocalizedName().contains("skull") ||
            item.getUnlocalizedName().contains("torch") ||
            item.getUnlocalizedName().contains("anvil") ||
            item.getUnlocalizedName().contains("enchant") ||
            item.getUnlocalizedName().contains("exp") ||
            item.getUnlocalizedName().contains("shears");
    }

}
