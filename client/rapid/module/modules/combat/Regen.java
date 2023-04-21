package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(getName = "Regen", getCategory = Category.COMBAT)
public class Regen extends Module {
    private final Setting
            packets = new Setting("Packets", this, 10, 1, 100, true),
            onHealth = new Setting("On Health", this, 15, 1, 19, true);

    public Regen() {
        add(packets, onHealth);
    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre() && mc.thePlayer.onGround && mc.thePlayer.getHealth() < onHealth.getValue() && mc.thePlayer.getHealth() != 0) {
            for(int i = 0; i < packets.getValue(); i++)
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
        }
    }
}