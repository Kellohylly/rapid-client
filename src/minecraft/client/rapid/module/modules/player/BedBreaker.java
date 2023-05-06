package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventRotation;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.block.BlockData;
import client.rapid.util.block.BlockUtil;
import client.rapid.util.module.RotationUtil;
import client.rapid.util.module.ScaffoldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(getName = "Bed Breaker", getCategory = Category.PLAYER)
public class BedBreaker extends Module {
    private final Setting range = new Setting("Range", this, 3, 3, 6, false);
    private final Setting delay = new Setting("Delay", this, 400, 10, 1000, true);
    private final Setting swing = new Setting("Swing", this, false);
    private final Setting rotate = new Setting("Rotate", this, true);
    private final Setting instantBreak = new Setting("Instant Break", this, true);
    private final Setting instantDisable = new Setting("Instantly Disable", this, false);

    private final TimerUtil timer = new TimerUtil();

    public BedBreaker() {
        add(range, delay, swing, rotate, instantBreak, instantDisable);
    }

    @Override
    public void onEvent(Event e) {
        double range = this.range.getValue();
        BlockPos blockPos = BlockUtil.getBlock(Blocks.bed, range);

        if(e instanceof EventRotation) {
            EventRotation event = (EventRotation)e;

            if(blockPos != null) {
                float[] rots = RotationUtil.getScaffoldRotations(blockPos, EnumFacing.UP);

                if (rotate.isEnabled()) {
                    event.setYaw(rots[0]);
                    event.setPitch(rots[1]);
                    mc.thePlayer.rotationYawHead = rots[0];
                    mc.thePlayer.rotationPitchHead = rots[1];
                }
            }
        }
        if(e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion) e;

            if(blockPos != null) {
                if (timer.sleep((int) delay.getValue())) {
                    if (swing.isEnabled())
                        mc.thePlayer.swingItem();
                    else
                        mc.getNetHandler().addToSendQueue(new C0APacketAnimation());

                    if(instantBreak.isEnabled()) {
                        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                    } else {
                        mc.playerController.onPlayerDamageBlock(blockPos, EnumFacing.NORTH);
                    }
                }
            }
            if(instantDisable.isEnabled())
                setEnabled(false);
        }
    }
}
