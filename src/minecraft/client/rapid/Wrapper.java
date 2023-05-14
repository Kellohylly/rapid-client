package client.rapid;

import client.rapid.command.CommandManager;
import client.rapid.config.*;
import client.rapid.module.ModuleManager;
import client.rapid.module.settings.SettingsManager;
import client.rapid.rpc.RichPresence;
import viamcp.ViaMCP;

public class Wrapper {
	private static CommandManager commandManager;
	private static SettingsManager settingsManager;
	private static ModuleManager moduleManager;

	private static Config config;
	private static DragConfig dragConfig;
	private static KeyConfig keyConfig;

	private static RichPresence richPresence;

	public static void init() {
		Wrapper.settingsManager = new SettingsManager();
		Wrapper.moduleManager = new ModuleManager();
		Wrapper.config = new Config();
		Wrapper.dragConfig = new DragConfig();
		Wrapper.keyConfig = new KeyConfig();
		Wrapper.commandManager = new CommandManager();
		Wrapper.richPresence = new RichPresence();
		config.generate();

		try {
			ViaMCP.getInstance().start();
			ViaMCP.getInstance().initAsyncSlider();
		} catch(Exception e) {
			e.printStackTrace();
		}
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
	
	public static Config getConfig() {
		return config;
	}

	public static DragConfig getDragConfig() {
		return dragConfig;
	}

	public static KeyConfig getKeyConfig() {
		return keyConfig;
	}

	public static RichPresence getRichPresence() {
		return richPresence;
	}
}
