package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.jesus.JesusBase;
import client.rapid.module.modules.movement.jesus.JesusMode;
import client.rapid.module.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;

@ModuleInfo(getName = "Jesus", getCategory = Category.MOVEMENT)
public class Jesus extends Module {
    private final Setting mode = new Setting("Mode", this, "Solid", "Jump", "Matrix");

    private final Setting jumpMode = new Setting("Jump", this, "Verus", "Vulcan");

    private JesusBase currentMode;

    public Jesus() {
        add(mode, jumpMode);
    }

    @Override
    public void onEnable() {
        this.setMode();
        currentMode.onEnable();
    }

    @Override
    public void onDisable() {
        currentMode.onDisable();
    }

    @Override
    public void settingCheck() {
        jumpMode.setVisible(mode.getMode().equals("Jump"));
    }

    @Override
    public void onEvent(Event e) {
        this.setMode();

        if (mode.getMode().equals("Jump")) {
            setTag(jumpMode.getMode());
        } else {
            setTag(mode.getMode());
        }


        if(isWaterBelow() && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.isSneaking()) {
            if(!mode.getMode().equals("Matrix") && mc.thePlayer.isInWater()) {
                return;
            }
            currentMode.onEvent(e);
        }
    }

    public boolean isWaterBelow() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        return isWater(new BlockPos(x, y - 1, z)) || isWater(new BlockPos(x, y - 2, z)) || isWater(new BlockPos(x, y, z));
    }

    private boolean isWater(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();

        return block instanceof BlockLiquid && block.getMaterial() == Material.water;
    }

    private void setMode() {
        for(JesusMode fm : JesusMode.values()) {
            if(currentMode != fm.getBase()) {
                switch(mode.getMode()) {
                    case "Solid":
                        if (mode.getMode().equals("Solid")) {
                            currentMode = JesusMode.SOLID.getBase();
                        }
                        break;
                    case "Jump":
                        if (jumpMode.getMode().equals(fm.getName())) {
                            currentMode = fm.getBase();
                        }
                        break;
                    case "Matrix":
                        if (mode.getMode().equals("Matrix")) {
                            currentMode = JesusMode.MATRIX_MOTION.getBase();
                        }
                        break;

                }
            }
        }
    }

}
