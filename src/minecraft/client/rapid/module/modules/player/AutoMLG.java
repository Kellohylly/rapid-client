package client.rapid.module.modules.player;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventRotation;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

@ModuleInfo(getName = "Auto MLG", getCategory = Category.PLAYER)
public class AutoMLG extends Module {
    private final Setting fallDistance = new Setting("Fall Distance", this, 4, 3, 10, false);
    private final Setting waterBucket = new Setting("Water Bucket", this, true);
    private final Setting pickupWater = new Setting("Pickup Water", this, false);
    private final Setting boat = new Setting("Boat", this, false);
    private final Setting leaveBoat = new Setting("Leave Boat", this, false);

    private BlockPos placedPos;
    private boolean clutched;

    private final TimerUtil timer = new TimerUtil();

    private float[] rotations = new float[2];
    
    public AutoMLG() {
        add(fallDistance, waterBucket, pickupWater, boat, leaveBoat);
    }

    @Override
    public void updateSettings() {
        pickupWater.setVisible(waterBucket.isEnabled());
        leaveBoat.setVisible(boat.isEnabled());
    }

    @Override
    public void onEnable() {
        placedPos = null;
        clutched = false;
    }

    @Override
    public void onDisable() {
        placedPos = null;
        clutched = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion) e;

            if(mc.thePlayer.fallDistance >= fallDistance.getValue()) {
                if(waterBucket.isEnabled()) {
                    for (int y = -3; y < 0; y++) {
                        BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);

                        if (!mc.theWorld.isAirBlock(blockPos)) {
                        	rotations = (RotationUtil.getScaffoldRotations(blockPos, EnumFacing.NORTH));
                            mc.thePlayer.rotationPitchHead = rotations[0];

                            for (int i = 0; i < 9; i++) {
                                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                                if (stack != null && stack.stackSize != 0 && stack.getItem() instanceof ItemBucket) {
                                    mc.thePlayer.inventory.currentItem = i;
                                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                                    placedPos = blockPos;
                                 }
                            }
                        }
                    }
                }
                if(boat.isEnabled()) {
                    for(Entity entity : mc.theWorld.loadedEntityList) {
                        if(entity instanceof EntityBoat && mc.thePlayer.getDistanceToEntity(entity) <= 3) {
                            PacketUtil.sendPacketSilent(new C02PacketUseEntity(entity, new Vec3(0.5, 0.5, 0.5)));
                            PacketUtil.sendPacketSilent(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT));
                            clutched = true;
                        }
                    }
                }
            } else {
                if(placedPos != null && pickupWater.isEnabled()) {
                    mc.thePlayer.rotationPitchHead = event.pitch;

                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                        if (stack != null && stack.stackSize != 0 && stack.getItem() instanceof ItemBucket) {
                            mc.thePlayer.inventory.currentItem = i;
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                            placedPos = null;
                        }
                    }
                }

                if(clutched && leaveBoat.isEnabled()) {
                    if(timer.reached(700)) {
                        mc.gameSettings.keyBindSneak.pressed = false;
                        clutched = false;
                        timer.reset();
                    } else
                        mc.gameSettings.keyBindSneak.pressed = true;
                } else
                    timer.reset();
            }
        }
        if(e instanceof EventRotation) {
        	EventRotation rotationEvent = (EventRotation)e;
        	
            if(mc.thePlayer.fallDistance >= fallDistance.getValue()) {
                if(mc.thePlayer.fallDistance >= fallDistance.getValue()) {
                    if(waterBucket.isEnabled()) {
                    	rotationEvent.setPitch(rotations[1]);
                    	rotationEvent.setYaw(rotations[0]);
                    }
                }
            } else {
                if(placedPos != null && pickupWater.isEnabled()) {
                	rotationEvent.setPitch(90);
                }
            }
        }
        rotations = new float[2];
    }
}
