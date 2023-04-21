package client.rapid.config;

import java.io.*;
import java.util.ArrayList;

import client.rapid.Wrapper;
import client.rapid.module.Module;
import net.minecraft.client.Minecraft;

public class KeyConfig {
	private final File dir = new File(Minecraft.getMinecraft().mcDataDir, "Rapid"), dataFile = new File(dir, "keybinds.keyb");
	
	public KeyConfig() {
		if (!dir.exists())
			dir.mkdir();

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.load();
	}
	
	public void save() {
		ArrayList<String> toSave = new ArrayList<>();
		
		for (Module mod : Wrapper.getModuleManager().getModules())
			toSave.add(mod.getName2() + ":" + mod.getKey());
		
		try {
			PrintWriter pw = new PrintWriter(this.dataFile);
			for (String str : toSave) {
				pw.println(str);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		ArrayList<String> lines = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
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
			Module m = Wrapper.getModuleManager().getModule2(args[0]);
			if (m != null)
				m.setKey(Integer.parseInt(args[1]));

			System.out.println(args[1]);

		}
	}
	
}
