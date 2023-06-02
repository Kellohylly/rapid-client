package client.rapid.module;

import client.rapid.*;
import client.rapid.event.Event;
import client.rapid.event.events.game.EventWorldLoad;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.animation.Animation;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Module {
	protected final String name = getClass().getAnnotation(ModuleInfo.class).getName();
	protected final Category category = getClass().getAnnotation(ModuleInfo.class).getCategory();
	
	protected String tag;
	protected int key;
	protected boolean enabled;

	private final Animation animY = new Animation(0, 0.6f);

	protected final Minecraft mc;
	
	public Module() {
		this.mc = Minecraft.getMinecraft();
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void onEvent(Event e) {}
	public void updateSettings() {}
	
	public void toggle() {
		setEnabled(!enabled);
	}
	
	public void add(Setting... settings) {
		Arrays.stream(settings).forEach(Client.getInstance().getSettingsManager()::rSetting);
	}

	public boolean isEnabled(Class<? extends Module> module) {
		return Client.getInstance().getModuleManager().getModule(module).isEnabled();
	}

	public String getMode(Class<? extends Module> module, String name) {
		return Client.getInstance().getSettingsManager().getSetting(module, name).getMode();
	}

	public boolean getBoolean(Class<? extends Module> module, String name) {
		return Client.getInstance().getSettingsManager().getSetting(module, name).isEnabled();
	}

	public double getDouble(Class<? extends Module> module, String name) {
		return Client.getInstance().getSettingsManager().getSetting(module, name).getValue();
	}

	public String getTag() {
		return tag != null ? tag : "";
	}

	public String getTag2() {
		return tag != null ? " " + tag: "";
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
		
		if(Client.getInstance() != null && mc.thePlayer != null) {
			Client.getInstance().getConfigManager().getModKeyConfig().save();
		}
	}

	public Animation getAnimY() {
		return animY;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if(mc.thePlayer != null) {
			if(enabled) 
				onEnable();
			else 
				onDisable();
		}
		save();
	}

	protected void autoDisable(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.thePlayer.getHealth() <= 0) {
				setEnabled(false);
			}
		}
		if (e instanceof EventWorldLoad) {
			setEnabled(false);
		}
	}

	public String getName() {
		return name;
	}

	public String getName2() {
		return name.replace(" ", "");
	}

	public Category getCategory() {
		return category;
	}
    
    protected static void save() {
		if(Client.getInstance().getConfigManager() != null && Minecraft.getMinecraft().thePlayer != null)
			Client.getInstance().getConfigManager().getModuleConfig().save();
    }
	
}
