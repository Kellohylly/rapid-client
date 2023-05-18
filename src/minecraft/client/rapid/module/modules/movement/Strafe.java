package client.rapid.module.modules.movement;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.MoveUtil;

@ModuleInfo(getName = "Strafe", getCategory = Category.MOVEMENT)
public class Strafe extends Module {
    private final Setting damageOnly = new Setting("Damage Only", this, false);

    public Strafe() {
        add(damageOnly);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(damageOnly.isEnabled()) {
                if(mc.thePlayer.hurtTime > 1)
                    setMoveSpeed(getMoveSpeed());
            } else
                setMoveSpeed(getMoveSpeed());
        }
    }
}
