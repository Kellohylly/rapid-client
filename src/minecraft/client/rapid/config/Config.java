package client.rapid.config;

import net.minecraft.client.Minecraft;

import java.io.File;

public class Config {
    private final String name;
    private final File data;

    public Config(String name) {
        this.name = name;
        this.data = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "Rapid" + File.separator + name);
    }

    public String getName() {
        return name;
    }

    public File getData() {
        return data;
    }

    public void save() {}
    public void load() {}

}
