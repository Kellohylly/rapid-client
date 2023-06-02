package client.rapid.module.settings;

import java.util.ArrayList;

import client.rapid.module.Module;
import client.rapid.util.console.Logger;

public class SettingsManager {
	private final ArrayList<Setting> settings = new ArrayList<>();

	public void rSetting(Setting in) {
		this.settings.add(in);
	}

	public ArrayList<Setting> getSettings() {
		return this.settings;
	}

	public ArrayList<Setting> getSettingsByMod(Module mod) {
		ArrayList<Setting> out = new ArrayList<Setting>();
		for (Setting s : getSettings()) {
			if (s.getParentMod().equals(mod))
				out.add(s);
		}
		if (out.isEmpty()) return null;
		return out;
	}

	public Setting getSetting(Class<? extends Module> mod, String name) {
		for (Setting set : getSettings()) {
			if (set.getName().equalsIgnoreCase(name) && set.getParentMod().getClass().equals(mod))
				return set;
		}
		Logger.error("Setting named \"" + name + "\" not found!");
		return null;
	}

	public Setting getSettingByName(String mod, String name) {
		for (Setting set : getSettings()) {
			if (set.getName().equalsIgnoreCase(name) && set.getParentMod().getName().equals(mod))
				return set;
		}
		Logger.error("Setting named \"" + name + "\" not found!");
		return null;
	}
}