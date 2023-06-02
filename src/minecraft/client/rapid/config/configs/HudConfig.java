package client.rapid.config.configs;

import client.rapid.Client;
import client.rapid.module.Draggable;
import client.rapid.module.modules.hud.Watermark;
import client.rapid.config.Config;
import client.rapid.util.console.Logger;

import java.io.*;
import java.util.ArrayList;

public class HudConfig extends Config {

    public HudConfig() {
        super("draggables.conf");
    }


    @Override
    public void save() {
        ArrayList<String> saves = new ArrayList<>();

        for(Draggable d : Client.getInstance().getModuleManager().getDraggables())
            saves.add("ModuleDrag:" + d.getName().replace(" ", "-") + ":" + d.getX() + ":" + d.getY());

        saves.add("WatermarkText:" + Watermark.text.replace(" ", "_"));

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
            Logger.error("Cant load config:" + e.getCause());
        }

        for(String line : lines) {
            String[] args = line.split(":");

            if(line.startsWith("ModuleDrag")) {
                Draggable d = (Draggable) Client.getInstance().getModuleManager().getModuleByName(args[1].replace("-", " "));

                if(d != null) {
                    d.setX(Integer.parseInt(args[2]));
                    d.setY(Integer.parseInt(args[3]));
                }
            }

            if(line.startsWith("WatermarkText"))
                Watermark.setWatermark(args[1].replace("_", " "));
        }
        super.load();
    }

}
