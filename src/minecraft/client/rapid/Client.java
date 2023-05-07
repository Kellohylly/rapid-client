package client.rapid;

import client.rapid.module.Module;

public class Client {
	private static final Client instance = new Client();

	public void init() {
		Wrapper.init();
	}

	public void stop() {
		Wrapper.stop();
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

}
