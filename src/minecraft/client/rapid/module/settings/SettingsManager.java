package client.rapid.module.settings;

import java.util.ArrayList;

import client.rapid.module.Module;
import client.rapid.util.console.Logger;

public class SettingsManager {
	private final ArrayList<Setting> settings = new ArrayList<>();

	public void rSetting(Setting setting) {
		this.settings.add(setting);
	}

	public ArrayList<Setting> getSettings() {
		return settings;
	}

	public ArrayList<Setting> getSettingsByMod(Module mod) {
		ArrayList<Setting> out = new ArrayList<>();

		settings.stream()
			.filter(s -> s.getParentMod() == mod)
			.forEach(out::add);

		if (out.isEmpty()) {
			return null;
		}

		return out;
	}

	public Setting getSetting(Class<? extends Module> mod, String name) {
		for (Setting setting : getSettings()) {
			if (setting.getName().equalsIgnoreCase(name) && setting.getParentMod().getClass().equals(mod)) {
				return setting;
			}
		}
		Logger.error("Setting named \"" + name + "\" not found!");
		return null;
	}

	public Setting getSettingByName(String mod, String name) {
		for (Setting set : getSettings()) {
			if (set.getName().equalsIgnoreCase(name) && set.getParentMod().getName().equals(mod)) {
				return set;
			}
		}

		Logger.error("Setting named \"" + name + "\" not found!");
		return null;
	}

}