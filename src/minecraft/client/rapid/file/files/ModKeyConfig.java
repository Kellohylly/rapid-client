package client.rapid.file.files;

import client.rapid.Wrapper;
import client.rapid.module.Module;
import client.rapid.file.File;

import java.io.*;
import java.util.ArrayList;

public class ModKeyConfig extends File {

    public ModKeyConfig() {
        super("keybinds.keyb");
    }

    @Override
    public void save() {
        ArrayList<String> toSave = new ArrayList<>();

        for (Module mod : Wrapper.getModuleManager().getModules())
            toSave.add(mod.getName() + ":" + mod.getKey());

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

        for (String s : lines) {
            String[] args = s.split(":");
            Module m = Wrapper.getModuleManager().getModule(args[0]);
            if (m != null)
                m.setKey(Integer.parseInt(args[1]));
        }
        super.load();
    }

}
