package client.rapid;

import client.rapid.command.CommandManager;
import client.rapid.config.*;
import client.rapid.module.ModuleManager;
import client.rapid.module.settings.SettingsManager;

public class Wrapper {
	private static final SettingsManager settingsManager = new SettingsManager();
	private static final ModuleManager moduleManager = new ModuleManager();
	private static final CommandManager commandManager = new CommandManager();
	
	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public static ModuleManager getModuleManager() {
		return moduleManager;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}
	
	public static Config getConfig() {
		return Client.getInstance().getConfig();
	}
}
