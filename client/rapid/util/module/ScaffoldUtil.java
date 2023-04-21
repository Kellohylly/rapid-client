package client.rapid.util.module;

import client.rapid.util.MinecraftUtil;
import client.rapid.util.block.BlockData;
import net.minecraft.block.Block;
import net.minecraft.util.*;

import java.util.List;

public class ScaffoldUtil extends MinecraftUtil {

    public static BlockData getBlockData(BlockPos pos, List<Block> invalid) {
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock()))
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock()))
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock()))
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock()))
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()))
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add = pos.add(-1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock()))
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock()))
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock()))
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()))
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);

        BlockPos add2 = pos.add(1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock()))
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock()))
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock()))
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock()))
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);

        BlockPos add3 = pos.add(0, 0, -1);
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock()))
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock()))
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock()))
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock()))
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add4 = pos.add(0, 0, 1);
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock()))
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock()))
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock()))
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock()))
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);

        return null;
    }

}
