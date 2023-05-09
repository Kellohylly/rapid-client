package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.BlockPos;

@ModuleInfo(getName = "Anti Fall", getCategory = Category.PLAYER)
public class AntiFall extends Module {
    private final Setting mode = new Setting("Mode", this, "Normal", "Jump", "Teleport");
    private final Setting fallDistance = new Setting("Fall Distance", this, 4, 2, 10, false);
    private final Setting height = new Setting("Height", this, 2, 0.42, 10, false);

    private BlockPos groundPos;

    public AntiFall() {
        add(mode, fallDistance, height);
    }

    @Override
    public void onEvent(Event e) {
        setTag(mode.getMode());

        if(e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion) e;

            if (mc.thePlayer.onGround && !mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)))
                groundPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

            if (mc.thePlayer.fallDistance >= fallDistance.getValue() && !PlayerUtil.isBlockUnder()) {
                switch (mode.getMode()) {
                    case "Normal":
                        event.setY(event.getY() + height.getValue());
                        break;
                    case "Jump":
                        event.setGround(!event.isGround());
                        mc.thePlayer.motionY = height.getValue();
                        mc.thePlayer.fallDistance = 0;
                        break;
                    case "Teleport":
                        if(mc.thePlayer.getDistance(groundPos.getX(), groundPos.getY(), groundPos.getZ()) <= 10) {
                            mc.thePlayer.setPosition(groundPos.getX(), groundPos.getY(), groundPos.getZ());
                            mc.thePlayer.fallDistance = 0;
                            setMoveSpeed(0);
                        }
                        break;
                }
            }
        }
    }
}
