package client.rapid.module.modules.movement.flights;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.module.modules.movement.Flight;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class FlightBase {
    protected final Setting speed = Client.getInstance().getSettingsManager().getSetting(Flight.class, "Speed");
    protected final Setting jump = Client.getInstance().getSettingsManager().getSetting(Flight.class, "Jump");
    protected final Setting damage = Client.getInstance().getSettingsManager().getSetting(Flight.class, "Damage");
    protected final Setting fast = Client.getInstance().getSettingsManager().getSetting(Flight.class, "Fast");

    protected Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
    public void updateValues() {}
}
