package client.rapid.module.modules.movement.longjumps;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class LongJumpBase {
    protected final Setting speed = Wrapper.getSettingsManager().getSettingByName("Long Jump", "Speed");
    protected final Setting damage = Wrapper.getSettingsManager().getSettingByName("Long Jump", "Damage");
    protected final Setting height = Wrapper.getSettingsManager().getSettingByName("Long Jump", "Height");

    protected Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
    public void updateValues() {}
}

