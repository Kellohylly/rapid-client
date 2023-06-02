package client.rapid.module.modules.movement.noslows;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.module.modules.movement.NoSlow;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class NoSlowBase {
    protected final Setting delay = Client.getInstance().getSettingsManager().getSetting(NoSlow.class, "Delay");

    protected Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
}
