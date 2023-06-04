package client.rapid.config.configs;

import client.rapid.Client;
import client.rapid.module.Module;
import client.rapid.module.settings.Setting;
import client.rapid.config.Config;
import client.rapid.util.console.Logger;

import java.io.*;
import java.util.ArrayList;

public class ModuleConfig extends Config {

    public ModuleConfig() {
        super("config.conf");
    }

    /*
    * honestly I have no idea if spaces don't fuck it up, but I doubt it.
    * last time it thought KillInsults was KillAura and didn't set KillAura's settings so i had to rename KillInsults
    * but it works for KeyConfig for some reason xd.
    */

    @Override
    public void save() {
        ArrayList<String> saves = new ArrayList<>();

        // Save module state
        for(Module m : Client.getInstance().getModuleManager().getModules()) {
            String modName = m.getName().replace(" ", "-");

            saves.add("Module:" + modName + ":" + m.isEnabled());

            for(Setting s : Client.getInstance().getSettingsManager().getSettings()) {
                if(s.getParentMod().getName().replace(" ", "-").equals(modName)) {

                    String setName = s.getName().replace(" ", "-");

                    if (s.isCheckbox()) {
                        saves.add("Setting:" + modName + ":" + setName + ":" + s.isEnabled());
                    }

                    if (s.isCombo()) {
                        saves.add("Setting:" + modName + ":" + setName + ":" + s.getMode().replace(" ", "-"));
                    }

                    if (s.isSlider()) {
                        saves.add("Setting:" + modName + ":" + setName + ":" + s.getValue());
                    }
                }
            }

            // Empty line to make the config file more readable
            saves.add("");
        }

        // Write file
        try {
            PrintWriter pw = new PrintWriter(this.getData());

            for (String str : saves) {
                pw.println(str);
            }

            pw.close();
        } catch (FileNotFoundException e) {
            Logger.error("Cant save config:" + e.getCause());
        }
        super.save();
    }

    @Override
    public void load() {
        this.load(this.getData());
        super.load();
    }

    // Load a specific file
    public void load(File file) {
        ArrayList<String> lines = new ArrayList<>();

        // Read file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            Logger.error("Cant load config:" + e.getCause());
        }

        // Load module state
        for(String line : lines) {
            String[] args = line.split(":");

            if(line.startsWith("Module")) {
                Module m = Client.getInstance().getModuleManager().getModuleByName(args[1].replace("-", " "));

                if (m != null) {
                    m.setEnabled(Boolean.parseBoolean(args[2]));
                }
            }

            // Load settings
            if(line.startsWith("Setting")) {
                Module m = Client.getInstance().getModuleManager().getModuleByName(args[1].replace("-", " "));

                if(m != null) {
                    Setting s = Client.getInstance().getSettingsManager().getSettingByName(m.getName().replace("-", " "), args[2].replace("-", " "));

                    if(s != null) {

                        if (s.isCheckbox()) {
                            s.setEnabled(Boolean.parseBoolean(args[3]));
                        }

                        if (s.isCombo()) {
                            s.setMode(args[3].replace("-", " "));
                        }

                        if (s.isSlider()) {
                            s.setValue(Double.parseDouble(args[3]));
                        }
                    }
                }
            }
        }
    }

}
