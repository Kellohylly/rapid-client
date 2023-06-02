package client.rapid.module.modules.movement.flights.verus;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.flights.FlightBase;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.util.AxisAlignedBB;

public class VerusFastFlight extends FlightBase {
    private int ticks;
    private double startY;

    private boolean canFly;
    private boolean jumped;
    private boolean canSpeed;

    private final TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        startY = mc.thePlayer.posY;

        if(mc.thePlayer.onGround) {
            canSpeed = true;
            ticks = 9;
            canFly = true;
        } else {
            canFly = false;
            NotificationManager.add("Flight", "This fly only works on ground", NotificationType.INFO, 3);
        }
    }

    @Override
    public void onDisable() {
        ticks = 0;
        jumped = false;
    }

    @Override
    public void updateValues() {
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide) e;

            if(canFly) {
                if (mc.gameSettings.keyBindJump.isKeyDown() && MoveUtil.isMoving()) {
                    event.setAxisAlignedBB(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getX(), event.getY(), event.getZ()));
                } else {
                    event.setAxisAlignedBB(new AxisAlignedBB(-2, 0, -2, 2, 0, 2).offset(event.getX(), startY, event.getZ()));
                }
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            boolean sprinting = mc.thePlayer.isSprinting();
            boolean jumpKeyDown = mc.gameSettings.keyBindJump.isKeyDown();

            mc.thePlayer.capabilities.isFlying = false;

            if(mc.thePlayer.onGround) {
                startY = mc.thePlayer.posY;
            }

            if(mc.thePlayer.onGround && !jumpKeyDown) {
                ticks++;

                if(ticks >= 10) {
                    mc.thePlayer.jump();
                    ticks = 0;
                    canSpeed = true;
                    jumped = true;
                }
                if(canSpeed) {
                    if(sprinting) {
                        if (mc.thePlayer.moveForward > 0) {
                            MoveUtil.setMoveSpeed(0.45);
                        } else {
                            MoveUtil.setMoveSpeed(0.42);
                        }
                    } else {
                        MoveUtil.setMoveSpeed(0.3);
                    }
                }

                if(canSpeed && !MoveUtil.isMoving()) {
                    canSpeed = false;
                }

                canFly = true;
            } else {
                if(jumped && !jumpKeyDown) {
                    if(MoveUtil.isMoving()) {
                        mc.thePlayer.motionY = -0.0980000019;
                    }
                    jumped = false;
                }

                MoveUtil.setMoveSpeed(sprinting ? 0.35 : 0.3);
            }
        }
    }

}
