package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(getName = "Ping Spoof", getCategory = Category.OTHER)
public class PingSpoof extends Module {
    private final Setting delay = new Setting("Delay", this, 4000, 100, 5000, true);

    private final List<Packet> packets = new ArrayList<>();

    private final TimerUtil timer = new TimerUtil();

    public PingSpoof() {
        add(delay);
    }

    public void onEnable() {
        packets.clear();
    }

    public void onDisable() {
        packets.clear();
    }

    public void onEvent(Event e) {
        if(e instanceof EventPacket && e.isPre()) {
            EventPacket event = (EventPacket)e;

            if(event.getPacket() instanceof C00PacketKeepAlive || event.getPacket() instanceof C0FPacketConfirmTransaction) {
                packets.add(event.getPacket());
                event.cancel();
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            if(packets.size() > 500) {
                for(int i = 1; i < packets.size(); i++) {
                    packets.remove(i);
                }
            }
            if(timer.sleep((int)delay.getValue()) && packets.size() >= 1) {
                PacketUtil.sendPacket(packets.get(0));
                packets.remove(0);
            }
        }
    }
}
