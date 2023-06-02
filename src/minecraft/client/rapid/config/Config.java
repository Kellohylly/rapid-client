package client.rapid.config;

import net.minecraft.client.Minecraft;

public class Config {
    private final String name;
    private final java.io.File data;

    // TODO: Fix path not being correct
    public Config(String name) {
        this.name = name;
        this.data = new java.io.File(Minecraft.getMinecraft().mcDataDir + java.io.File.separator + "Rapid" + java.io.File.separator + name);
    }

    public String getName() {
        return name;
    }

    public java.io.File getData() {
        return data;
    }

    public void save() {}
    public void load() {}
}
