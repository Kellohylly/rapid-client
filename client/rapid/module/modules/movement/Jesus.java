package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.*;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.*;

@ModuleInfo(getName = "Jesus", getCategory = Category.MOVEMENT)
public class Jesus extends Module {
    private final Setting
    mode = new Setting("Mode", this, "Solid"),
    jumpMode = new Setting("Jump", this, "None", "Normal", "Vulcan");

    private boolean walking;
    private int ticks;

    public Jesus() {
        add(mode, jumpMode);
    }

    public void onDisable() {
        walking = false;
        ticks = 0;
    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            setTag(mode.getMode());
            ticks++;

            if(walking) {
                switch(jumpMode.getMode()) {
                case "Normal":
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
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide)e;
            if (mc.theWorld == null || mc.thePlayer.fallDistance > 3 || (mc.thePlayer.isBurning() && PlayerUtil.isOnWater()))
                return;

            if(!(event.getBlock() instanceof BlockLiquid) || mc.thePlayer.isInWater() || mc.thePlayer.isSneaking())
                return;

            switch(mode.getMode()) {
                case "Solid":
                    event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
            }
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
