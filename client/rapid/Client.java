package client.rapid;

import client.rapid.config.*;
import client.rapid.module.Module;
import client.rapid.rpc.RichPresence;

public class Client {
	private static final Client instance = new Client();
	private static final Config config = new Config();
	private static final KeyConfig keyConfig = new KeyConfig();
	private final RichPresence richPresence = new RichPresence();

	public void init() {
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
		return "1.1.2";
	}
	
	public Config getConfig() {
		return config;
	}

	public KeyConfig getKeyConfig() {
		return keyConfig;
	}

	public RichPresence getRichPresence() {
		return richPresence;
	}
}
