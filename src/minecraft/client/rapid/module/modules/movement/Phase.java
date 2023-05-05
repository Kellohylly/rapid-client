package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;

@ModuleInfo(getName = "Phase", getCategory = Category.MOVEMENT)
public class Phase extends Module {
    private final Setting mode = new Setting("Mode", this, "Vanilla");
    private int reset;
    private double dist = 1D;

    public Phase() {
        add(mode);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()) {
            switch(mode.getMode()) {
                case "Vanilla":
                reset -= 1;
                double xOff;
                double zOff;
                double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
                double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
                xOff = mc.thePlayer.moveForward * 2.6D * mx + mc.thePlayer.moveStrafing * 2.6D * mz;
                zOff = mc.thePlayer.moveForward * 2.6D * mz + mc.thePlayer.moveStrafing * 2.6D * mx;

                if (PlayerUtil.isInsideBlock() && mc.thePlayer.isSneaking())
                    reset = 1;

                if (reset > 0)
                    mc.thePlayer.setPosition(mc.thePlayer.posX + xOff, mc.thePlayer.posY, mc.thePlayer.posZ + zOff);
                break;
            }
        }
        if(e instanceof EventCollide && e.isPre()) {
            EventCollide event = (EventCollide)e;

            if((mc.gameSettings.keyBindJump.isKeyDown()) || (!PlayerUtil.isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && mc.thePlayer.isSneaking()))
                ((EventCollide)e).setBoundingBox(null);
        }
    }
}
