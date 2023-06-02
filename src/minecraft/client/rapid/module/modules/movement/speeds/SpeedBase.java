package client.rapid.module.modules.movement.speeds;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.module.modules.movement.Speed;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class SpeedBase {
    protected final Setting speed = Client.getInstance().getSettingsManager().getSetting(Speed.class, "Speed");
    protected final Setting damageBoost = Client.getInstance().getSettingsManager().getSetting(Speed.class, "Damage Boost");
    protected final Setting groundStrafe = Client.getInstance().getSettingsManager().getSetting(Speed.class, "Ground Strafe");

    protected static Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}

}
