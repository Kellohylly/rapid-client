package client.rapid.util;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public class PacketUtil extends MinecraftUtil {

    public static void sendPacket(Packet<? extends INetHandler> packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static void sendPacketSilent(Packet<? extends INetHandler> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

}
