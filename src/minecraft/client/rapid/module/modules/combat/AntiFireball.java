package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventRotation;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;

@ModuleInfo(getName = "Anti Fireball", getCategory = Category.COMBAT)
public class AntiFireball extends Module {
    private final Setting range = new Setting("Range", this, 3, 2, 6, false);
    private final Setting delay = new Setting("Delay", this, 40, 0, 100,false);
    private final Setting rotate = new Setting("Rotate", this, false);

    private final TimerUtil timer = new TimerUtil();

    public AntiFireball() {
        add(range, delay, rotate);
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onEvent(Event e) {
        Entity fireball = null;
        
        for(Entity entity : mc.theWorld.loadedEntityList) {
            if(entity instanceof EntityFireball && mc.thePlayer.getDistanceToEntity(entity) < range.getValue() + 4) {
                fireball = entity;
            }
        }

        if(e instanceof EventRotation && fireball != null && rotate.isEnabled()) {
            EventRotation event = (EventRotation) e;

            float[] rots = RotationUtil.getRotations(fireball, 0, 0);

            event.setYaw(rots[0]);
            event.setPitch(rots[1]);

            mc.thePlayer.rotationYawHead = event.getYaw();
            mc.thePlayer.rotationPitchHead = event.getPitch();
        }
        if(e instanceof EventUpdate && e.isPre()) {
            if(fireball != null && mc.thePlayer.getDistanceToEntity(fireball) < range.getValue() && timer.reached((long) (delay.getValue() * 10))) {
                mc.playerController.attackEntity(mc.thePlayer, fireball);
                timer.reset();
            }
        }
    }
}
