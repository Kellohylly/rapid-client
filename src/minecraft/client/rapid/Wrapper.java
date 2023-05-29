package client.rapid;

import client.rapid.command.CommandManager;
import client.rapid.file.FileManager;
import client.rapid.module.ModuleManager;
import client.rapid.module.settings.SettingsManager;
import client.rapid.rpc.RichPresence;
import viamcp.ViaMCP;

public class Wrapper {
	private static CommandManager commandManager;
	private static SettingsManager settingsManager;
	private static ModuleManager moduleManager;

	private static FileManager configManager;

	private static RichPresence richPresence;

	public static void preinit() {
		Wrapper.richPresence = new RichPresence();

		try {
			ViaMCP.getInstance().start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		Wrapper.settingsManager = new SettingsManager();
		Wrapper.moduleManager = new ModuleManager();
		Wrapper.configManager = new FileManager();
		Wrapper.commandManager = new CommandManager();
		ViaMCP.getInstance().initAsyncSlider();
	}

	public static void stop() {
		Wrapper.richPresence.stop();
	}

	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public static ModuleManager getModuleManager() {
		return moduleManager;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static FileManager getConfigManager() {
		return configManager;
	}

	public static RichPresence getRichPresence() {
		return richPresence;
	}
}
