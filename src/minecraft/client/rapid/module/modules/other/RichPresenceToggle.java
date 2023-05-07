package client.rapid.module.modules.other;

import client.rapid.Wrapper;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Rich Presence", getCategory = Category.OTHER)
public class RichPresenceToggle extends Module {

    @Override
    public void onEnable() {
        Wrapper.getRichPresence().init();
    }

    @Override
    public void onDisable() {
        Wrapper.getRichPresence().stop();
    }

}
