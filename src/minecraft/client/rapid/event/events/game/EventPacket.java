package client.rapid.event.events.game;

import client.rapid.event.Event;

import net.minecraft.network.Packet;

public class EventPacket extends Event {

	private Packet<?> packet;
	private final boolean incoming;
	
	public EventPacket(Packet<?> packet, boolean incoming) {
		this.packet = packet;
		this.incoming = incoming;
	}
	
	public <T extends Packet<?>> T getPacket() {
		return (T) packet;
	}
	
	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}
	
	public boolean isIncoming() {
		return incoming;
	}

}
