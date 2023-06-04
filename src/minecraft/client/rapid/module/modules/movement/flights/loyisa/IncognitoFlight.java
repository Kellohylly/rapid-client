package client.rapid.module.modules.movement.flights.loyisa;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.flights.FlightBase;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.module.MoveUtil;
import io.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class IncognitoFlight extends FlightBase {
    private int hurts;

    @Override
    public void onEnable() {
        NotificationManager.add("Flight", "Use Verus No Fall on 3.0 distance", NotificationType.INFO, 3);
        hurts = 0;
    }

    @Override
    public void onDisable() {
        hurts = 0;
        MoveUtil.setMoveSpeed(0.2);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
           if(mc.thePlayer.hurtTime == 1) {
                mc.thePlayer.motionY -= 1f;
                hurts++;
            }
            if(mc.thePlayer.hurtTime == 0) {
                MoveUtil.setMoveSpeed(MoveUtil.getMoveSpeed() + 0.2);
            }
            if(mc.thePlayer.hurtTime > 1) {
                mc.thePlayer.motionY = 0.65;

                if(hurts == 0) {
                    MoveUtil.setMoveSpeed(0.1);
                }
            }
            if(hurts > 0 && mc.thePlayer.hurtTime > 0) {
                MoveUtil.setMoveSpeed(3.1);
            }
            MoveUtil.strafe();

        }
    }

}
