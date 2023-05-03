package client.rapid;

import client.rapid.command.CommandManager;
import client.rapid.config.*;
import client.rapid.module.ModuleManager;
import client.rapid.module.settings.SettingsManager;

public class Wrapper {
	private static final CommandManager commandManager = new CommandManager();
	
	public static SettingsManager getSettingsManager() {
		return Client.getInstance().getSettingsManager();
	}
	
	public static ModuleManager getModuleManager() {
		return Client.getInstance().getModuleManager();
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}
	
	public static Config getConfig() {
		return Client.getInstance().getConfig();
	}

	public static DragConfig getDragConfig() {
		return Client.getInstance().getDragConfig();
	}
}
