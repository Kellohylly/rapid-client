package client.rapid.module.modules.movement;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
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
			if(isEnabled("Scaffold") && !getBoolean("Scaffold", "Sprint"))
				return;

			if(omni.isEnabled()) {
				if(mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isBlocking() && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isEating() && !mc.thePlayer.isSneaking() && isMoving()) {
					mc.thePlayer.setSprinting(true);

					if(isMovingOnGround() && (mc.gameSettings.keyBindBack.isKeyDown() || isMoving() && mc.thePlayer.moveForward == 0) && !Wrapper.getModuleManager().getModule("Speed").isEnabled()) {
						mc.thePlayer.motionX *= 1.2f;
						mc.thePlayer.motionZ *= 1.2f;
					}
				}
			}
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
		}
	}
}
