package client.rapid.config;

import client.rapid.Wrapper;
import client.rapid.module.Draggable;
import client.rapid.module.modules.visual.Hud;
import client.rapid.util.console.Logger;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

public class DragConfig {
	private final File
	dir = new File(Minecraft.getMinecraft().mcDataDir, "Rapid"),
	data = new File(dir, "draggables.conf");

	private char sep = ':';

	public DragConfig() {
		if(!dir.exists())
			dir.mkdir();

		if(!data.exists()) {
			try {
				data.createNewFile();
			} catch (IOException e) {
				Logger.error("Something went wrong: " + e.getCause());
			}
		} else
			load(data);
	}

	public void save() {
		ArrayList<String> saves = new ArrayList<>();

		for(Draggable d : Wrapper.getModuleManager().getDraggables())
			saves.add("ModuleDrag" + sep + d.getName().replace(" ", "-") + sep + d.getX() + sep + d.getY());

		saves.add("WatermarkText" + sep + Hud.text.replace(" ", "ag]68j$"));

		try {
			PrintWriter pw = new PrintWriter(data);

			for (String str : saves)
				pw.println(str);

			pw.close();
		} catch (FileNotFoundException e) {
			Logger.error("Cant save config:" + e.getCause());
		}

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
			String[] args = line.split(String.valueOf(sep));

			if(line.startsWith("ModuleDrag")) {
				Draggable d = (Draggable) Wrapper.getModuleManager().getModule(args[1].replace("-", " "));

				if(d != null) {
					d.setX(Integer.parseInt(args[2]));
					d.setY(Integer.parseInt(args[3]));
				}
			}

			if(line.startsWith("WatermarkText"))
				Hud.setWatermark(args[1].replace("ag]68j$", " "));
		}
	}

	public void generate() {
		File file = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "Rapid" + File.separator + "insults.txt");

		if(!file.exists()) {
			try {
				file.createNewFile();

				FileWriter writer = new FileWriter(file);
				writer.write("bro got sent home.\nyou are the type of person to use FDP and lose.\nChuck Norris once stepped on kid, Its descendants were then known as degenerates like you.\nShh, he doesnt know i know this hack (Rapid Client)!\nYou cant win, I have 5 million power!\nyou seem like a guy who deep throats ice cream.");
				writer.close();
			} catch (IOException e) {
				Logger.error("Cant generate insults.txt");
			}
		}

		file = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "Rapid" + File.separator + "spammer.txt");

		if(!file.exists()) {
			try {
				file.createNewFile();

				FileWriter writer = new FileWriter(file);
				writer.write("get spammed lol\ntrash server\nimagine paying to win\nwhat the cat doing\ncat supremacy\ncats >>");
				writer.close();
			} catch (IOException e) {
				Logger.error("Cant generate spammer.txt");
			}
		}
	}
	
}
