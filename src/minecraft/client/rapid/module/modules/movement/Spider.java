package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Spider", getCategory = Category.MOVEMENT)
public class Spider extends Module {
    private final Setting mode = new Setting("Mode", this, "Vanilla");
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

        if(e instanceof EventUpdate && e.isPre() && mode.getMode().equals("Vanilla") && canClimb())
            mc.thePlayer.motionY = speed.getValue() / 2;
    }

    private boolean canClimb() {
        return mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder();
    }
}
