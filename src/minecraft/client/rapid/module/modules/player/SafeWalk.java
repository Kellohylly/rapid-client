package client.rapid.module.modules.player;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventSafewalk;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Safe Walk", getCategory = Category.PLAYER)
public class SafeWalk extends Module {

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventSafewalk && e.isPre())
            e.cancel();
    }
}
