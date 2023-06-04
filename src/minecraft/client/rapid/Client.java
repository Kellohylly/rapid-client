package client.rapid;

import client.rapid.command.CommandManager;
import client.rapid.config.ConfigManager;
import client.rapid.module.Module;
import client.rapid.module.ModuleManager;
import client.rapid.module.settings.SettingsManager;
import client.rapid.rpc.DiscordRP;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import viamcp.ViaMCP;

public class Client {

	private static final Client instance = new Client();

	// Client information
	private final String clientName = "Rapid";
	private final String clientVersion = "1.6";

	// Discord Rich Presence
	private DiscordRP discordRP;

	// Managers
	private SettingsManager settingsManager;
	private ModuleManager moduleManager;
	private ConfigManager configManager;
	private CommandManager commandManager;

	// Executes before game
	public void onPreStartup() {
		this.discordRP = new DiscordRP();

		// Initialize VIA
		try {
			ViaMCP.getInstance().start();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	// Execute after main menus
	public void onStartup() {

		// Initialize managers
		this.settingsManager = new SettingsManager();
		this.moduleManager = new ModuleManager();
		this.configManager = new ConfigManager();
		this.commandManager = new CommandManager();

		// Initialize VIA Version slider
		ViaMCP.getInstance().initAsyncSlider();
	}

	// Executes when game stops
	public void onShutdown() {
		this.discordRP.stopRPC();
	}

	// Executes when player presses a key
	public void onKeyPressed(int key) {
		this.moduleManager.getModules().stream()
			.filter(m -> m.getKey() == key)
			.forEach(Module::toggle);
	}

	// Send a message client-side
	public void addChatMessage(String message) {
		String prefix = "§cRapid §7» " + "§r";

		ChatComponentText chatComponent = new ChatComponentText(prefix + message);

		Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
	}

	public static Client getInstance() {
		return instance;
	}
	
	public String getName() {
		return clientName;
	}

	public String getVersion() {
		return clientVersion;
	}

	public DiscordRP getDiscordRP() {
		return discordRP;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

}
