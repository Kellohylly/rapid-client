package client.rapid.module.modules.movement.longjumps;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.module.modules.movement.LongJump;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;

public class LongJumpBase {
    protected final Setting speed = Client.getInstance().getSettingsManager().getSetting(LongJump.class, "Speed");
    protected final Setting damage = Client.getInstance().getSettingsManager().getSetting(LongJump.class, "Damage");
    protected final Setting height = Client.getInstance().getSettingsManager().getSetting(LongJump.class, "Height");

    protected Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
    public void updateValues() {}
}

