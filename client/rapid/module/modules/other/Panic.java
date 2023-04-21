package client.rapid.module.modules.other;

import client.rapid.Wrapper;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

import java.util.Objects;

@ModuleInfo(getName = "Panic", getCategory = Category.OTHER)
public class Panic extends Module {
	private final Setting removeKeybindings = new Setting("Remove Keybindings", this, true);

	public Panic() {
		add(removeKeybindings);
	}
	
	public void onEnable() {
		for(Module m : Wrapper.getModuleManager().getModules()) {
			if(m.isEnabled() && !Objects.equals(m.getName(), name))
				m.toggle();
			
			if(removeKeybindings.isEnabled())
				m.setKey(0);
		}
	}
}
