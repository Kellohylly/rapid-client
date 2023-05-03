package client.rapid.util;

import net.minecraft.network.Packet;

public class PacketUtil extends MinecraftUtil {

    public static void sendPacket(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static void sendPacketSilent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
