package client.rapid.module.modules.movement.flights;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class FlightBase {
    protected final Setting speed = Wrapper.getSettingsManager().getSettingByName("Flight", "Speed");
    protected final Setting jump = Wrapper.getSettingsManager().getSettingByName("Flight", "Jump");
    protected final Setting damage = Wrapper.getSettingsManager().getSettingByName("Flight", "Damage");
    protected final Setting fast = Wrapper.getSettingsManager().getSettingByName("Flight", "Fast");

    protected Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
    public void updateValues() {}
}
