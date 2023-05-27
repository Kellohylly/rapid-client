package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventWorldLoad;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@ModuleInfo(getName = "Jesus", getCategory = Category.MOVEMENT)
public class Jesus extends Module {
    private final Setting mode = new Setting("Mode", this, "Solid", "Jump", "Vulcan", "Matrix");

    private boolean walking;
    private int ticks;

    public Jesus() {
        add(mode);
    }

    @Override
    public void onDisable() {
        walking = false;
        ticks = 0;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventWorldLoad) {
            walking = false;
            ticks = 0;
        }
        if(e instanceof EventUpdate && e.isPre()) {
            setTag(mode.getMode());
            ticks++;

            if(walking) {
                switch(mode.getMode()) {
                case "Jump":
                   if(mc.thePlayer.onGround && !isEnabled("Speed"))
                        mc.thePlayer.jump();
                    break;
                case "Vulcan":
                    if(mc.thePlayer.onGround && !isEnabled("Speed")) {
                        mc.thePlayer.jump();
                        ticks = 0;
                    }

                    if(mc.thePlayer.isInWater())
                        return;

                    if (ticks > 9) {
                        mc.gameSettings.keyBindForward.pressed = false;
                        mc.thePlayer.setSprinting(false);
                        setMoveSpeed(0);
                    } else if(ticks > 1) {
                        mc.gameSettings.keyBindForward.pressed = true;
                        setMoveSpeed(getBaseMoveSpeed());
                        mc.thePlayer.setSprinting(true);
                    }
                    break;
                }
            }
            if(mode.getMode().equals("Matrix")) {
                if(mc.thePlayer.isInWater()) {
                    if(mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.motionY = 0.22;
                    } else {
                        mc.thePlayer.motionY = 0.13;
                    }
                    mc.gameSettings.keyBindJump.pressed = false;
                }
            }
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide)e;
            if (mc.theWorld == null || mc.thePlayer.fallDistance > 3 || (mc.thePlayer.isBurning() && PlayerUtil.isOnWater()) || mode.getMode().equals("Matrix"))
                return;

            if(!(event.getBlock() instanceof BlockLiquid) || mc.thePlayer.isInWater() || mc.thePlayer.isSneaking())
                return;

            event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
            walking = (PlayerUtil.isOnWater() || isOnWater()) && !mc.thePlayer.isInWater();
        }
    }

    public boolean isOnWater() {
        final double y = mc.thePlayer.posY - 1;
        for (int x = MathHelper.floor_double(mc.thePlayer.posX); x < MathHelper.ceiling_double_int(mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(mc.thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid &&mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.water)
                    return true;
            }
        }
        return false;
    }
}
