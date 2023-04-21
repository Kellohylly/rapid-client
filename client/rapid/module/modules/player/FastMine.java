package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(getName =  "Fast Mine", getCategory = Category.PLAYER)
public class FastMine extends Module {
    private final Setting amplifier = new Setting("Amplifier", this, 2, 0, 2, true);

    public FastMine() {
        add(amplifier);
    }

    public void onDisable() {
        mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre() && mc.thePlayer.getHeldItem() != null) {
            if(hasTool() && mc.gameSettings.keyBindAttack.isKeyDown())
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, 2));
            else
                mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
        }
    }

    private boolean hasTool() {
        return mc.thePlayer.getHeldItem().getItem() instanceof ItemTool && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword);
    }
}
