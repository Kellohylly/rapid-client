package client.rapid.config.configs;

import client.rapid.Client;
import client.rapid.module.Module;
import client.rapid.config.Config;

import java.io.*;
import java.util.ArrayList;

public class ModKeyConfig extends Config {

    public ModKeyConfig() {
        super("keybinds.keyb");
    }

    @Override
    public void save() {
        ArrayList<String> toSave = new ArrayList<>();

        // Save module key
        for (Module mod : Client.getInstance().getModuleManager().getModules()) {
            toSave.add(mod.getName() + ":" + mod.getKey());
        }

        // Write file
        try {
            PrintWriter pw = new PrintWriter(this.getData());
            for (String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        super.save();
    }

    @Override
    public void load() {
        ArrayList<String> lines = new ArrayList<>();

        // Read file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.getData()));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set module key
        for (String s : lines) {
            String[] args = s.split(":");

            Module m = Client.getInstance().getModuleManager().getModuleByName(args[0]);

            if (m != null) {
                m.setKey(Integer.parseInt(args[1]));
            }
        }
        super.load();
    }

}
