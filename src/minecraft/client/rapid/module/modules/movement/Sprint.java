package client.rapid.module.modules.movement;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.player.Scaffold;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.MoveUtil;
import net.minecraft.client.settings.KeyBinding;

@ModuleInfo(getName = "Sprint", getCategory = Category.MOVEMENT)
public class Sprint extends Module {
	private final Setting omni = new Setting("Omni", this, false);

	public Sprint() {
		add(omni);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(isEnabled(Scaffold.class) && getMode(Scaffold.class, "Sprint").equals("None"))
				return;

			if((mc.thePlayer.isBlocking() || mc.thePlayer.isUsingItem() || mc.thePlayer.isEating()) && !getBoolean(NoSlow.class, "Allow Sprinting"))
				return;

			if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.thePlayer.isCollidedHorizontally)
				mc.thePlayer.setSprinting(true);

			if(omni.isEnabled()) {
				if(mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isBlocking() && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isEating() && !mc.thePlayer.isSneaking() && MoveUtil.isMoving()) {
					mc.thePlayer.setSprinting(true);

					if(MoveUtil.isMovingOnGround() && (mc.gameSettings.keyBindBack.isKeyDown() || MoveUtil.isMoving() && mc.thePlayer.moveForward == 0) && !Client.getInstance().getModuleManager().getModule(Speed.class).isEnabled()) {
						mc.thePlayer.motionX *= 1.2f;
						mc.thePlayer.motionZ *= 1.2f;
					}
				}
			}
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
		}
	}
}
