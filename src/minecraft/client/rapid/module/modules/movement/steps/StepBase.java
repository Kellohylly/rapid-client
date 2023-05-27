package client.rapid.module.modules.movement.steps;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.module.modules.movement.Step;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import net.minecraft.client.Minecraft;

public class StepBase {
    protected final Setting height = Wrapper.getSettingsManager().getSettingByName("Step", "Height");

    protected final TimerUtil timer = ((Step)Wrapper.getModuleManager().getModule("Step")).timer;

    protected static Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
}
