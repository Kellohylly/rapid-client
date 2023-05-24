package client.rapid.module.modules.movement.speeds;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class SpeedBase {
    protected final Setting speed = Wrapper.getSettingsManager().getSettingByName("Speed", "Speed");
    protected final Setting damageBoost = Wrapper.getSettingsManager().getSettingByName("Speed", "Damage Boost");
    protected final Setting groundStrafe = Wrapper.getSettingsManager().getSettingByName("Speed", "Ground Strafe");

    protected static Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}

}
