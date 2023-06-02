package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.AxisAlignedBB;

@ModuleInfo(getName = "Spider", getCategory = Category.MOVEMENT)
public class Spider extends Module {
    private final Setting mode = new Setting("Mode", this, "Vanilla", "Collide");
    private final Setting speed = new Setting("Speed", this, 2, 0.1, 5, false);
    private final Setting jumpOnly = new Setting("Jump Only", this, false);

    public Spider() {
        add(mode, speed, jumpOnly);
    }

    @Override
    public void onEvent(Event e) {
        setTag(mode.getMode());
        if(jumpOnly.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())
            return;

        if(e instanceof EventUpdate && e.isPre() && canClimb()) {
            switch(mode.getMode()) {
                case "Vanilla":
                    mc.thePlayer.motionY = speed.getValue() / 2;
                    break;
                case "Collide":
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                    break;
            }
        }

        if(e instanceof EventCollide && e.isPre() && canClimb()) {
            EventCollide event = (EventCollide) e;

            if(!PlayerUtil.isInsideBlock()) {
                if(mc.thePlayer.motionY > 0.0)
                    return;

                event.setAxisAlignedBB(new AxisAlignedBB(event.getX(), event.getY(), event.getZ(), event.getX() + 1, 1, event.getZ() + 1));

            } else {
                mc.thePlayer.motionX *= -1f;
                mc.thePlayer.motionZ *= -1f;
            }

        }
    }

    private boolean canClimb() {
        return mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder();
    }

}
