package client.rapid.module.modules.player;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.RotationUtil;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

@ModuleInfo(getName = "Chest Aura", getCategory = Category.PLAYER)
public class ChestAura extends Module {
    private final Setting range = new Setting("Range", this, 3, 3, 6, false);
    private final Setting delay = new Setting("Open Delay", this, 200, 10, 300, true);
    private final Setting enderChests = new Setting("Ender Chests", this, false);
    private final Setting rememberChest = new Setting("Remember Chest", this, true);
    private final Setting swing = new Setting("Swing", this, false);
    private final Setting rotate = new Setting("Rotate", this, true);

    private final ArrayList<BlockPos> chests = new ArrayList<>();

    private final TimerUtil timer = new TimerUtil();


    public ChestAura() {
        add(range, delay, enderChests, rememberChest, swing, rotate);
    }

    @Override
    public void onEnable() {
        chests.clear();
        timer.reset();
    }

    @Override
    public void onDisable() {
        chests.clear();
        timer.reset();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion) e;

            for(TileEntity entity : mc.theWorld.loadedTileEntityList) {
                if(entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
                    if(!enderChests.isEnabled() && entity instanceof TileEntityEnderChest)
                        return;

                    BlockPos blockPos = new BlockPos(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());
                    Vec3 vec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                    if(rememberChest.isEnabled() && chests.contains(blockPos))
                        return;

                    if(mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < range.getValue()) {
                        float[] rots = RotationUtil.getScaffoldRotations(blockPos, EnumFacing.UP);

                        if(rotate.isEnabled()) {
                            event.setYaw(rots[0]);
                            event.setPitch(rots[1]);
                            mc.thePlayer.rotationYawHead = event.yaw;
                            mc.thePlayer.renderYawOffset = event.yaw;
                            mc.thePlayer.rotationPitchHead = event.pitch;
                        }

                        if(timer.sleep((int) delay.getValue() * 10L) && mc.currentScreen == null) {
                            if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockPos, EnumFacing.UP, vec)) {

                                if(swing.isEnabled())
                                    mc.thePlayer.swingItem();
                                else
                                    PacketUtil.sendPacket(new C0APacketAnimation());

                                chests.add(blockPos);
                            }
                        }
                    }
                }
            }
        }
    }
}