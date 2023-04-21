package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(getName = "No Rotate", getCategory = Category.PLAYER)
public class NoRotate extends Module {
	
	public void onEvent(Event e) {
		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket)e;
			
			boolean illegalRotateCheck = mc.thePlayer.rotationYaw != -180 && mc.thePlayer.rotationPitch != 0;

	        if (!event.isIncoming() && event.getPacket() instanceof S08PacketPlayerPosLook && illegalRotateCheck) {
				S08PacketPlayerPosLook packet = event.getPacket();

            	packet.setYaw(mc.thePlayer.rotationYaw);
            	packet.setPitch(mc.thePlayer.rotationPitch);
	        }
		}
	}
}
