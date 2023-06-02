package client.rapid.module.modules.movement.noslows.ncp;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventSlowdown;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.modules.movement.noslows.NoSlowBase;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class OldNCPNoSlow extends NoSlowBase {
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

            if(KillAura.target == null && e.isPre()) {
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.isUsingItem()) {
                    PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                }
            }

            event.cancel();
        }
    }

}
