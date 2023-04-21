package client.rapid.util.block;


import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockData {
    private BlockPos position;
    private EnumFacing face;

    public BlockData(BlockPos position, EnumFacing face) {
        this.position = position;
        this.face = face;
    }

    public BlockPos getPosition() {
        return position;
    }

    public void setPosition(BlockPos position) {
        this.position = position;
    }

    public EnumFacing getFace() {
        return face;
    }

    public void setFace(EnumFacing face) {
        this.face = face;
    }
}
