package client.rapid.config;

import client.rapid.config.configs.HudConfig;
import client.rapid.config.configs.ModKeyConfig;
import client.rapid.config.configs.ModuleConfig;
import client.rapid.util.console.Logger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private final ModuleConfig moduleConfig;
    private final ModKeyConfig modKeyConfig;
    private final HudConfig hudConfig;

    private final File dir;

    public ConfigManager() {
        this.dir = new File(Minecraft.getMinecraft().mcDataDir, "Rapid");

        this.generateFiles();

        this.moduleConfig = new ModuleConfig();
        this.modKeyConfig = new ModKeyConfig();
        this.hudConfig = new HudConfig();

        for(Config config : new Config[] {moduleConfig, modKeyConfig, hudConfig}) {
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
        File file = new File(dir, File.separator + "insults.txt");

        if(!file.exists()) {
            try {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);

                writer.write(
                    "{name} got sent home." +
                    "\n{name} is the type of person to use FDP and lose." +
                    "\nChuck Norris once stepped on kid, Its descendants were then known as degenerates like {name}." +
                    "\n{name}, you seem like a guy who deep throats ice cream."
                );

                writer.close();

            } catch (IOException e) {
                Logger.error("Cant generate insults.txt");
            }
        }

        // Spammer File
        file = new File(dir, File.separator + "spammer.txt");

        if(!file.exists()) {
            try {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);

                writer.write(
                    "get spammed lol" +
                    "\ntrash server" +
                    "\nimagine paying to win" +
                    "\nwhat the cat doing" +
                    "\ncat supremacy" +
                    "\ncats >>"
                );

                writer.close();

            } catch (IOException e) {
                Logger.error("Cant generate spammer.txt");
            }
        }

        file = new File(dir, File.separator + "Configs");

        if(!file.exists()) {
            file.mkdir();
        }

    }

}
