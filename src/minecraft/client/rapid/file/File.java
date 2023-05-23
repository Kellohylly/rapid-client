package client.rapid.file;

import net.minecraft.client.Minecraft;

public class File {
    private final String name;
    private final java.io.File data;

    // TODO: Fix path not being correct
    public File(String name) {
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
