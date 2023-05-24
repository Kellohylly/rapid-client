package client.rapid.module.modules.movement.flights.verus;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.flights.FlightBase;
import net.minecraft.util.AxisAlignedBB;

public class CollideFlight extends FlightBase {
    private double startY;

    @Override
    public void onEnable() {
        this.startY = mc.thePlayer.posY;
    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(mc.thePlayer.onGround) {
                if(jump.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.jump();
                }

                startY = mc.thePlayer.posY;
            }
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide) e;

            if(jump.isEnabled()) {
                event.setBoundingBox(new AxisAlignedBB(-2, 0, -2, 2, 0, 2).offset(event.getX(), startY, event.getZ()));

                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
                }
            } else {
                event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
            }
        }
    }

}
