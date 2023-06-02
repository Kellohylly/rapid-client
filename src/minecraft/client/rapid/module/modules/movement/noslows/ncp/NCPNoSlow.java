package client.rapid.module.modules.movement.noslows.ncp;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventSlowdown;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.modules.movement.noslows.NoSlowBase;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class NCPNoSlow extends NoSlowBase {
    private final TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSlowdown) {
            EventSlowdown event = (EventSlowdown) e;

            if(mc.thePlayer.getHeldItem() != null && KillAura.target == null) {
                if(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {

                    if (e.isPre()) {
                        PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
                        PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    }
                    if (e.isPost() && timer.sleep((int)delay.getValue())) {
                        PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }

                    if(mc.thePlayer.onGround) {
                        event.cancel();
                    }
                }
            }

            if(KillAura.target != null) {
                event.cancel();
            }
        }
    }
}
