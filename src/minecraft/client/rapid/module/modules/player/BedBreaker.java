package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.RotationUtil;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;

@ModuleInfo(getName = "Bed Breaker", getCategory = Category.PLAYER)
public class BedBreaker extends Module {
    private final Setting range = new Setting("Range", this, 3, 3, 6, false);
    private final Setting delay = new Setting("Delay", this, 400, 10, 1000, true);
    private final Setting swing = new Setting("Swing", this, false);
    private final Setting rotate = new Setting("Rotate", this, true);
    private final Setting instantDisable = new Setting("Instantly Disable", this, false);

    private final TimerUtil timer = new TimerUtil();

    public BedBreaker() {
        add(range, delay, swing, rotate, instantDisable);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion) e;

            double range = this.range.getValue();

            for(double x = -range; x < range; x++) {
                for(double y = -range; y < range; y++) {
                    for (double z = -range; z < range; z++) {
                        BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);

                        if(mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed) {
                            float[] rots = RotationUtil.getScaffoldRotations(blockPos, EnumFacing.NORTH);

                            if(rotate.isEnabled()) {
                                event.setYaw(rots[0]);
                                event.setPitch(rots[1]);
                            }

                            if(timer.sleep((int)delay.getValue())) {
                                PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                                PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));

                                if (swing.isEnabled())
                                    mc.thePlayer.swingItem();
                                else
                                    mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                            }
                        }
                    }
                }
            }
            if(instantDisable.isEnabled())
                setEnabled(false);
        }
    }
}
