package client.rapid.file;

import client.rapid.file.files.HudConfig;
import client.rapid.file.files.ModKeyConfig;
import client.rapid.file.files.ModuleConfig;
import client.rapid.util.console.Logger;
import net.minecraft.client.Minecraft;

import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private final ModuleConfig moduleConfig;
    private final ModKeyConfig modKeyConfig;
    private final HudConfig hudConfig;

    private final java.io.File dir;

    public FileManager() {
        this.dir = new java.io.File(Minecraft.getMinecraft().mcDataDir, "Rapid");

        this.generateFiles();

        this.moduleConfig = new ModuleConfig();
        this.modKeyConfig = new ModKeyConfig();
        this.hudConfig = new HudConfig();

        for(File config : new File[] {moduleConfig, modKeyConfig, hudConfig}) {
            if(!config.getData().exists()) {
                try {
                    config.getData().createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    config.load();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ModuleConfig getModuleConfig() {
        return moduleConfig;
    }

    public ModKeyConfig getModKeyConfig() {
        return modKeyConfig;
    }

    public HudConfig getHudConfig() {
        return hudConfig;
    }

    public void generateFiles() {

        // Insults File
        java.io.File file = new java.io.File(dir, java.io.File.separator + "insults.txt");

        if(!file.exists()) {
            try {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write("bro got sent home." +
                        "\nyou are the type of person to use FDP and lose." +
                        "\nChuck Norris once stepped on kid, Its descendants were then known as degenerates like you." +
                        "\nShh, he doesnt know i know this hack (Rapid Client)!" +
                        "\nYou cant win, I have 5 million power!" +
                        "\nyou seem like a guy who deep throats ice cream.");
                writer.close();
            } catch (IOException e) {
                Logger.error("Cant generate insults.txt");
            }
        }

        // Spammer File
        file = new java.io.File(dir, java.io.File.separator + "spammer.txt");

        if(!file.exists()) {
            try {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write("get spammed lol" +
                        "\ntrash server" +
                        "\nimagine paying to win" +
                        "\nwhat the cat doing" +
                        "\ncat supremacy" +
                        "\ncats >>");
                writer.close();
            } catch (IOException e) {
                Logger.error("Cant generate spammer.txt");
            }
        }

        file = new java.io.File(dir, java.io.File.separator + "Configs");

        if(!file.exists()) {
            file.mkdir();
        }

    }

}
