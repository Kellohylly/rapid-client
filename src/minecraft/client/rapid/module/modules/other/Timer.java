package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.MathUtil;

@ModuleInfo(getName = "Timer", getCategory = Category.OTHER)
public class Timer extends Module {
    private final Setting speed = new Setting("Speed", this, 1, 0.1, 3, false),
    random = new Setting("Random", this, 1, 0, 1, false);

    public Timer() {
        add(speed, random);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre())
            mc.timer.timerSpeed = (float) speed.getValue() + (float)MathUtil.randomNumber(random.getValue(), -random.getValue());
    }
}
