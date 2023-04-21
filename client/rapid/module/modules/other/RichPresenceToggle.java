package client.rapid.module.modules.other;

import client.rapid.Client;
import client.rapid.module.*;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Rich Presence", getCategory = Category.OTHER)
public class RichPresenceToggle extends Module {

    public void onEnable() {
        Client.getInstance().getRichPresence().init();
    }

    public void onDisable() {
        Client.getInstance().getRichPresence().stop();
    }
}
