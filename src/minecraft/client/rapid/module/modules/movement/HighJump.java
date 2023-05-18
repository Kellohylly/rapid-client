package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.util.EnumChatFormatting;

@ModuleInfo(getName = "High Jump", getCategory = Category.MOVEMENT)
public class HighJump extends Module {
    private final Setting mode = new Setting("Mode", this, "Boat", "Water");
    private final Setting height = new Setting("Height", this, 1, 0.5, 10, false);
    private final Setting speed = new Setting("Speed", this, 1, 0.5, 10, false);

    public HighJump() {
        add(mode, height, speed);
    }

    @Override
    public void onEnable() {
        if(!mc.thePlayer.isRiding() && mode.getMode().equals("Boat")) {
            PlayerUtil.addChatMessage(EnumChatFormatting.RED + "You must be in a boat to do this!");
            setEnabled(false);
        }
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            switch(mode.getMode()) {
                case "Boat":
                    if(!mc.thePlayer.isRiding()) {
                        mc.thePlayer.motionY = height.getValue();
                        setMoveSpeed(speed.getValue());
                        setEnabled(false);
                    }
                    break;
                case "Water":
                    if(mc.thePlayer.isInWater()) {
                        mc.thePlayer.motionY = height.getValue();
                        setMoveSpeed(speed.getValue());
                        setEnabled(false);
                    }
                    break;
            }
        }
    }
}
