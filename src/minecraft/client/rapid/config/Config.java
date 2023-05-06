package client.rapid.config;

import java.io.*;
import java.util.ArrayList;

import client.rapid.Wrapper;
import client.rapid.module.*;
import client.rapid.module.settings.Setting;
import client.rapid.util.console.Logger;
import net.minecraft.client.Minecraft;

public class Config {
	private final File
	dir = new File(Minecraft.getMinecraft().mcDataDir, "Rapid"),
	data = new File(dir, "config.conf");

	private char sep = ':';

	public Config() {
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

		for(Module m : Wrapper.getModuleManager().getModules()) {
			saves.add("Module" + sep + m.getName().replace(" ", "-") + sep + m.isEnabled());

			for(Setting s : Wrapper.getSettingsManager().getSettings()) {
				if(s.getParentMod().getName().replace(" ", "-").equals(m.getName().replace(" ", "-"))) {

					if (s.isCheck())
						saves.add("Setting" + sep + m.getName().replace(" ", "-") + sep + s.getName().replace(" ", "-") + sep + s.isEnabled());

					if (s.isCombo())
						saves.add("Setting" + sep + m.getName().replace(" ", "-") + sep + s.getName().replace(" ", "-") + sep + s.getMode().replace(" ", "-"));

					if (s.isSlider())
						saves.add("Setting" + sep + m.getName().replace(" ", "-") + sep + s.getName().replace(" ", "-") + sep + s.getValue());
				}
			}
		}

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

			if(line.startsWith("Module") && !line.startsWith("ModuleDrag")) {
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
