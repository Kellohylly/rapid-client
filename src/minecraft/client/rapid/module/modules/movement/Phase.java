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
    private final Setting mode = new Setting("Mode", this, "Vanilla", "No Clip");
    private final Setting speed = new Setting("Speed", this, 2.6, 0.25, 3, false);

    private int reset;

    private boolean wasPhasing;

    public Phase() {
        add(mode, speed);
    }

    @Override
    public void onEnable() {
        wasPhasing = false;
    }

    @Override
    public void onDisable() {
        wasPhasing = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()) {
            setTag(mode.getMode());

            switch(mode.getMode()) {
            case "Vanilla":
                reset -= 1;
                double xOff;
                double zOff;

                double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
                double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90F));

                xOff = mc.thePlayer.moveForward * speed.getValue() * mx + mc.thePlayer.moveStrafing * speed.getValue() * mz;
                zOff = mc.thePlayer.moveForward * speed.getValue() * mz + mc.thePlayer.moveStrafing * speed.getValue() * mx;

                if (PlayerUtil.isInsideBlock() && mc.thePlayer.isSneaking()) {
                    reset = 1;
                }

                if (reset > 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX + xOff, mc.thePlayer.posY, mc.thePlayer.posZ + zOff);
                }
                break;
                case "No Clip":
                    mc.thePlayer.noClip = true;

                    if(PlayerUtil.isInsideBlock()) {
                        mc.thePlayer.motionY = 0D;
                        mc.thePlayer.onGround = true;
                        setMoveSpeed(speed.getValue());
                        wasPhasing = true;
                    } else {
                        if(wasPhasing) {
                            setMoveSpeed(0);
                            wasPhasing = false;
                        }
                    }
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
