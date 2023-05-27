package client.rapid.module.modules.movement.steps.packet;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventStep;
import client.rapid.module.modules.movement.steps.StepBase;
import client.rapid.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class OldNCPStep extends StepBase {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventStep && e.isPre()) {
            EventStep event = (EventStep) e;

            double rheight = mc.thePlayer.getEntityBoundingBox().minY + 1 - mc.thePlayer.posY;

            if(mc.thePlayer.isCollidedHorizontally) {
                event.setHeight((float) height.getValue());
            } else {
                event.setHeight(0.6f);
            }
            if (rheight >= 0.8) {
                if (rheight <= 1) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4199, mc.thePlayer.posZ, false));
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7532, mc.thePlayer.posZ, false));
                    timer.reset();
                }
            }
        }
    }
}
