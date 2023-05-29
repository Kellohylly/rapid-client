package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleInfo(getName = "Lunar Spoofer", getCategory = Category.OTHER)
public class LunarSpoofer extends Module {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventPacket && e.isPre()) {
            EventPacket event = (EventPacket) e;

            if(event.getPacket() instanceof C17PacketCustomPayload) {
                C17PacketCustomPayload packet = event.getPacket();

                packet.setData(new PacketBuffer(Unpooled.wrappedBuffer(("Lunar").getBytes())));
            }
        }
    }

}
