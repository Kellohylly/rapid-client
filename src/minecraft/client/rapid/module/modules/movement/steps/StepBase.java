package client.rapid.module.modules.movement.steps;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.module.modules.movement.Step;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import net.minecraft.client.Minecraft;

public class StepBase {
    protected final Setting height = Client.getInstance().getSettingsManager().getSetting(Step.class, "Height");

    protected final TimerUtil timer = ((Step) Client.getInstance().getModuleManager().getModule(Step.class)).timer;

    protected static Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event e) {}
}
