package client.rapid.config.configs;

import client.rapid.Wrapper;
import client.rapid.gui.panelgui.component.Comp;
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

    @Override
    public void save() {
        ArrayList<String> saves = new ArrayList<>();

        for(Module m : Wrapper.getModuleManager().getModules()) {
            saves.add("Module:" + m.getName().replace(" ", "-") + ":" + m.isEnabled());

            for(Setting s : Wrapper.getSettingsManager().getSettings()) {
                if(s.getParentMod().getName().replace(" ", "-").equals(m.getName().replace(" ", "-"))) {

                    if (s.isCheck())
                        saves.add("Setting:" + m.getName().replace(" ", "-") + ":" + s.getName().replace(" ", "-") + ":" + s.isEnabled());

                    if (s.isCombo())
                        saves.add("Setting:" + m.getName().replace(" ", "-") + ":" + s.getName().replace(" ", "-") + ":" + s.getMode().replace(" ", "-"));

                    if (s.isSlider())
                        saves.add("Setting:" + m.getName().replace(" ", "-") + ":" + s.getName().replace(" ", "-") + ":" + s.getValue());
                }
            }
        }

        try {
            PrintWriter pw = new PrintWriter(this.getData());

            for (String str : saves)
                pw.println(str);

            pw.close();
        } catch (FileNotFoundException e) {
            Logger.error("Cant save config:" + e.getCause());
        }
        super.save();
    }

    @Override
    public void load() {
        System.out.println(this.getData());
        this.load(this.getData());
        super.load();
    }

    public void load(File file) {
        ArrayList<String> lines = new ArrayList<>();

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

        for(String line : lines) {
            String[] args = line.split(":");

            if(line.startsWith("Module")) {
                Module m = Wrapper.getModuleManager().getModule(args[1].replace("-", " "));

                if(m != null)
                    m.setEnabled(Boolean.parseBoolean(args[2]));
            }

            if(line.startsWith("Setting")) {
                Module m = Wrapper.getModuleManager().getModule(args[1].replace("-", " "));

                if(m != null) {
                    Setting s = Wrapper.getSettingsManager().getSettingByName(m.getName().replace("-", " "), args[2].replace("-", " "));

                    if(s != null) {
                        if(s.isCheck())
                            s.setEnabled(Boolean.parseBoolean(args[3]));

                        if(s.isCombo())
                            s.setMode(args[3].replace("-", " "));

                        if(s.isSlider())
                            s.setValue(Double.parseDouble(args[3]));
                    }
                }
            }
        }
    }

}
