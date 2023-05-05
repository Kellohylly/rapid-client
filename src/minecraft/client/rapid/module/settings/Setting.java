package client.rapid.module.settings;

import java.util.*;

import client.rapid.Client;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventSettingChange;
import client.rapid.module.Module;
import net.minecraft.client.Minecraft;

public class Setting {
	private ArrayList<String> options;

	private final String name;
	private final SettingType mode;

	private String sval;
	private final Module parent;
	private boolean bval, onlyInteger = false;
	private double dval, min, max;

	public Setting(String name, Module parent, String... options) {
		this.options = new ArrayList<>(Arrays.asList(options));
		this.name = name;
		this.parent = parent;
		this.sval = options[0];
		this.mode = SettingType.COMBO;
	}
	
	public Setting(String name, Module parent, boolean bval) {
		this.name = name;
		this.parent = parent;
		this.bval = bval;
		this.mode = SettingType.CHECK;
	}
	
	public Setting(String name, Module parent, double doubleValue, double min, double max, boolean onlyInteger) {
		this.name = name;
		this.parent = parent;
		this.dval = doubleValue;
		this.min = min;
		this.max = max;
		this.onlyInteger = onlyInteger;
		this.mode = SettingType.SLIDER;
	}
	
	public String getName() {
		return name;
	}
	
	public Module getParentMod() {
		return parent;
	}
	
	public String getMode() {
		return this.sval;
	}
	
	public void setMode(String in) {
		this.sval = in;
		save();

		Event.dispatch(new EventSettingChange(this));
	}
	
	public ArrayList<String> getOptions() {
		return this.options;
	}
	
	public boolean isEnabled() {
		return this.bval;
	}
	
	public void setEnabled(boolean in) {
		this.bval = in;
		save();

		Event.dispatch(new EventSettingChange(this));
	}

	public double getValue() {
		if(onlyInteger) dval = (int) dval;
		return this.dval;
	}

	public void setValue(double in) {
		this.dval = in;
		save();

		Event.dispatch(new EventSettingChange(this));
	}
	
	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
	
	public boolean isCombo() {
		return mode == SettingType.COMBO;
	}
	
	public boolean isCheck() {
		return mode == SettingType.CHECK;
	}
	
	public boolean isSlider() {
		return mode == SettingType.SLIDER;
	}
    
    private void save() {
		if(Client.getInstance() != null && Minecraft.getMinecraft().thePlayer != null)
			Client.getInstance().getConfig().save();
    }
}
