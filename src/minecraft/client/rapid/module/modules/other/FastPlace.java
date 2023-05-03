package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Fast Place", getCategory = Category.OTHER)
public class FastPlace extends Module {
    private final Setting delay = new Setting("Delay", this, 0, 0, 5, true);

    public FastPlace() {
        add(delay);
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 6;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            mc.rightClickDelayTimer = (int) delay.getValue();
        }
    }
}