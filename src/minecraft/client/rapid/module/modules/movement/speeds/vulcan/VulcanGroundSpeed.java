package client.rapid.module.modules.movement.speeds.vulcan;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.speeds.SpeedBase;
import client.rapid.util.MathUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class VulcanGroundSpeed extends SpeedBase {
    private int ticks;
    private double y;

    @Override
    public void onEnable() {
        ticks = 0;
        y = 0;
    }

    @Override
    public void onDisable() {
        ticks = 0;
        y = 0;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }

            if(ticks == 0) {
                y = 0;
                ticks++;
            }

            if(MoveUtil.isMovingOnGround()) {
                mc.thePlayer.motionY = 0.005;
                MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() + 0.13);

                y = 0.01 + MathUtil.randomNumber(0.01, 0.03);
                ticks = 0;
            }
        }
        if(e instanceof EventPacket && e.isPre()) {
            EventPacket event = (EventPacket) e;

            if(event.getPacket() instanceof C03PacketPlayer) {
                ((C03PacketPlayer) event.getPacket()).y += y;
            }
        }
    }

}
