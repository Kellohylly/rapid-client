package client.rapid;

import client.rapid.config.*;
import client.rapid.module.Module;
import client.rapid.module.ModuleManager;
import client.rapid.module.settings.SettingsManager;
import client.rapid.rpc.RichPresence;

public class Client {
	private static final Client instance = new Client();

	private SettingsManager settingsManager;
	private ModuleManager moduleManager;

	private Config config;
	private DragConfig dragConfig;
	private KeyConfig keyConfig;

	private RichPresence richPresence;

	public void init() {
		this.settingsManager = new SettingsManager();
		this.moduleManager = new ModuleManager();
		this.config = new Config();
		this.dragConfig = new DragConfig();
		this.keyConfig = new KeyConfig();
		this.richPresence = new RichPresence();
		config.generate();
	}

	public void stop() {
		richPresence.stop();
	}
	
	public void onKeyPressed(int key) {
		Wrapper.getModuleManager().getModules().stream().filter(m -> m.getKey() == key).forEach(Module::toggle);
	}

	public static Client getInstance() {
		return instance;
	}
	
	public String getName() {
		return "Rapid";
	}
	
	public String getVersion() {
		return "1.4.1";
	}
	
	public Config getConfig() {
		return config;
	}

	public DragConfig getDragConfig() {
		return dragConfig;
	}

	public KeyConfig getKeyConfig() {
		return keyConfig;
	}

	public RichPresence getRichPresence() {
		return richPresence;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}
}
