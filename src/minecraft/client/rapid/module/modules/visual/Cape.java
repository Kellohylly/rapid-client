package client.rapid.module.modules.visual;

import client.rapid.Client;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

import net.minecraft.util.ResourceLocation;

@ModuleInfo(getName = "Cape", getCategory = Category.VISUAL)
public class Cape extends Module {
    private final Setting mode = new Setting("Mode", this, "Rapid", "Hydrogen", "Lunar");

    public Cape() {
        add(mode);
    }

    public static ResourceLocation getCape() {
        switch (Client.getInstance().getSettingsManager().getSetting(Cape.class, "Mode").getMode()) {
            case "Hydrogen":
                return new ResourceLocation("rapid/images/hydrogen.png");
            case "Lunar":
                return new ResourceLocation("rapid/images/lunar.png");
        }
        return new ResourceLocation("rapid/images/cape.png");
    }

}
