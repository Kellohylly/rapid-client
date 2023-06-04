package client.rapid.module.modules.movement.longjumps.vulcan;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.movement.LongJump;
import client.rapid.module.modules.movement.longjumps.LongJumpBase;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.util.BlockPos;

public class VulcanClipLongJump extends LongJumpBase {
    private int ticks;

    private boolean jumped;

    private final TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        ticks = 0;

        if(!mc.thePlayer.onGround) {
            NotificationManager.add("Long Jump", "This mode must be used on ground", NotificationType.ERROR, 2);
            Client.getInstance().getModuleManager().getModule(LongJump.class).setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        ticks = 0;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide) e;

            if(ticks <= 8) {
                spoofing = true;
            }
            // this does better than setting position because it can fall inside multiple blocks and helps longjump to not be so short.
            if(ticks < 3) {
                event.setAxisAlignedBB(null);
            }
        }
        if(e instanceof EventUpdate && e.isPre()) {
            ticks++;

            if (ticks >= 7 && ticks < 9) {
                mc.thePlayer.motionY = height.getValue();

                if(!mc.thePlayer.onGround) {
                    MoveUtil.setMoveSpeed(3.4);
                }
            } else {
                if(ticks == 9) {
                    mc.thePlayer.motionY = 0;
                    MoveUtil.setMoveSpeed(0.32);
                }

                if(ticks > 9) {
                    if(timer.sleep(146)) {
                        mc.thePlayer.motionY = -0.1476;
                    } else {
                        mc.thePlayer.motionY = -0.0975;
                    }

                    if(!mc.thePlayer.onGround) {
                        if(mc.thePlayer.ticksExisted % 11 == 0) {
                            boolean canSpoof = mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ));

                            // Only spoof when no block below to ensure Auto Disable works
                            if(canSpoof) {
                                spoofing = true;
                                mc.thePlayer.onGround = true;
                            }

                            if (mc.gameSettings.keyBindForward.isKeyDown() && canSpoof) {
                                MoveUtil.setMoveSpeed(0.48);
                            }
                        } else {
                            spoofing = false;
                        }
                    }
                }
            }
        }
    }

}
