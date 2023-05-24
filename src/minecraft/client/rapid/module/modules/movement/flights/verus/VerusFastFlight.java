package client.rapid.module.modules.movement.flights.verus;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.Flight;
import client.rapid.module.modules.movement.flights.FlightBase;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class VerusFastFlight extends FlightBase {
    private int ticks;
    private boolean canFly;
    private boolean damaged;
    private double startY;

    private final TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        ticks = 0;

        if (mc.thePlayer.onGround) {
            if(damage.getMode().equals("None")) {
                mc.thePlayer.jump();
            }

            canFly = true;
        } else {
            NotificationManager.addToQueue(new Notification("Flight", "Verus fly only works on ground", NotificationType.WARNING, 3));
            canFly = false;
        }
    }

    @Override
    public void onDisable() {
        ticks = 0;
    }

    @Override
    public void updateValues() {
        this.damaged = Flight.damaged;
    }


    // TODO: Fix double motion jumping and not setting startY properly;
    @Override
    public void onEvent(Event e) {
        if(e instanceof EventPacket && e.isPre()) {
            EventPacket event = (EventPacket)e;

            if(event.getPacket() instanceof C03PacketPlayer && canFly && !timer.reached(575)) {
                ((C03PacketPlayer) event.getPacket()).setOnGround(true);
            }
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide) e;

            if (mc.thePlayer.isSneaking() || !canFly)
                return;

            if (mc.gameSettings.keyBindJump.isKeyDown() && MoveUtil.isMoving()) {
                event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
            } else {
                event.setBoundingBox(new AxisAlignedBB(-2, 0, -2, 2, 0, 2).offset(event.getX(), startY, event.getZ()));
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            ticks++;

            mc.thePlayer.capabilities.isFlying = false;

            // With damage
            if (ticks >= 3 && mc.thePlayer.hurtTime > 1) {
                MoveUtil.setMoveSpeed(speed.getValue());
                canFly = true;
                timer.reset();
            } else if (damaged) {
                if(timer.reached(1000)) {
                    MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed());
                } else {
                    MoveUtil.setMoveSpeed(speed.getValue());
                }
                if(canFly && timer.reached(575) && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
            }

            // Without damage
            if(mc.thePlayer.onGround) {
                if(canFly && !damaged)
                    mc.thePlayer.jump();

                startY = mc.thePlayer.posY;
            }
        }
    }

}
