package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@ModuleInfo(getName = "TP Aura", getCategory = Category.COMBAT)
public class TPAura extends Module {
    private final Setting range = new Setting("Range", this, 100, 10, 100, true);
    private Entity target;

    private final TimerUtil delay = new TimerUtil();

    public TPAura() {
        add(range);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            getTarget();

            if(target != null && delay.sleep(1000)) {
                BlockPos pos = target.getPosition();
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;

                // TODO: pathfinder or something to send player like 8 blocks closer every time the entity is too far.
                for(int i = 0; i < 10; i++) {
                    mc.thePlayer.setPosition(pos.getX(), pos.getY(), pos.getZ());
                }

                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, target);

                mc.thePlayer.setPosition(x, y, z);

            }
        }
    }

    private void getTarget() {
        for(Entity entity : mc.theWorld.playerEntities) {
            if(mc.thePlayer.getDistanceToEntity(entity) <= range.getValue() && entity != mc.thePlayer) {
                target = entity;
            }
        }
    }

}
