package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;

@ModuleInfo(getName = "Boat Jump", getCategory = Category.MOVEMENT)
public class BoatJump extends Module {
    private final Setting
    height = new Setting("Height", this, 1, 0.5, 10, false),
    speed = new Setting("Speed", this, 1, 0.5, 10, false);

    public BoatJump() {
        add(height, speed);
    }

    public void onEnable() {
        if(!mc.thePlayer.isRiding()) {
            PlayerUtil.addChatMessage(EnumChatFormatting.RED + "You must be in a boat to do this!");
            setEnabled(false);
        }
    }

    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(!mc.thePlayer.isRiding()) {
                mc.thePlayer.motionY = height.getValue();
                setMoveSpeed(speed.getValue());
                setEnabled(false);
            }
        }
    }
}
