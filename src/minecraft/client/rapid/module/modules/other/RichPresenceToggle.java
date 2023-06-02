package client.rapid.module.modules.other;

import client.rapid.Client;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Rich Presence", getCategory = Category.OTHER)
public class RichPresenceToggle extends Module {

    @Override
    public void onEnable() {
        Client.getInstance().getDiscordRP().startRPC();
    }

    @Override
    public void onDisable() {
        Client.getInstance().getDiscordRP().stopRPC();
    }

}
