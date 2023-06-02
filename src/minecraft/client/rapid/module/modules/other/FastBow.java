package client.rapid.module.modules.other;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.PacketUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Mouse;

@ModuleInfo(getName = "Fast Bow", getCategory = Category.OTHER)
public class FastBow extends Module {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(Mouse.isButtonDown(1) && canFastBow()) {
                for(int i = 0; i < 20; i++)
                    PacketUtil.sendPacket(new C03PacketPlayer());

                mc.playerController.onStoppedUsingItem(mc.thePlayer);
            }
        }
    }

    private boolean canFastBow() {
        return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.thePlayer.onGround;
    }
}
