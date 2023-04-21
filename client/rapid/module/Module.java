package client.rapid.module;

import client.rapid.*;
import client.rapid.event.events.Event;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class Module {
	protected final String name = getClass().getAnnotation(ModuleInfo.class).getName();
	protected final Category category = getClass().getAnnotation(ModuleInfo.class).getCategory();
	
	protected String tag;
	protected int key;
	protected boolean enabled;
	
	protected final Minecraft mc;
	
	public Module() {
		this.mc = Minecraft.getMinecraft();
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void onEvent(Event e) {}
	
	public void toggle() {
		setEnabled(!enabled);
	}
	
	public void add(Setting... settings) {
		for(Setting s : settings)
			Wrapper.getSettingsManager().rSetting(s);
	}

	public boolean isEnabled(String module) {
		return Wrapper.getModuleManager().getModule(module).isEnabled();
	}

	public String getMode(String module, String name) {
		return Wrapper.getSettingsManager().getSettingByName(module, name).getMode();
	}

	public boolean getBoolean(String module, String name) {
		return Wrapper.getSettingsManager().getSettingByName(module, name).isEnabled();
	}

	public double getValue(String module, String name) {
		return Wrapper.getSettingsManager().getSettingByName(module, name).getValue();
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
		
		if(Client.getInstance() != null && mc.thePlayer != null)
			Client.getInstance().getKeyConfig().save();
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

	public String getName() {
		return name;
	}

	public String getName2() {
		return name.replace(" ", "");
	}

	public Category getCategory() {
		return category;
	}
	
    protected void setMoveSpeed(double moveSpeed) {
    	setMoveSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }
    
    protected float getMoveSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }
    
    protected boolean isMoving() {
        return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
    }
    
    protected boolean isMovingOnGround() {
        return isMoving() && mc.thePlayer.onGround;
    }

    protected void setMoveSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            if (strafe > 0.0D)
            	yaw += (forward > 0.0D) ? -45 : 45;

            else if (strafe < 0.0D)
            	yaw += (forward > 0.0D) ? 45 : -45;

            strafe = 0.0D;

            if (forward > 0.0D)
            	forward = 1.0D;

            else if (forward < 0.0D)
            	forward = -1.0D;
        }
        if (strafe > 0.0D)
        	strafe = 1.0D;

        else if (strafe < 0.0D)
        	strafe = -1.0D;

        double
        mx = Math.cos(Math.toRadians(yaw + 90.0F)),
        mz = Math.sin(Math.toRadians(yaw + 90.0F));

        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    protected double getBaseMoveSpeed() {
        double base = 0.2875;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            base *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        
        return base;
    }
    
    protected static void save() {
		if(Client.getInstance().getConfig() != null && Minecraft.getMinecraft().thePlayer != null)
			Client.getInstance().getConfig().save();
    }
	
}
