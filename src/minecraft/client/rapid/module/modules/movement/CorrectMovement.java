package client.rapid.module.modules.movement;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventJump;
import client.rapid.event.events.player.EventStrafe;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.module.RotationUtil;

@ModuleInfo(getName = "Correct Movement", getCategory = Category.MOVEMENT)
public class CorrectMovement extends Module {

	@Override
	public void onEvent(Event e) {
        if (e instanceof EventStrafe) {
            ((EventStrafe) e).setYaw(RotationUtil.yaw);
        }
        if (e instanceof EventJump) {
            ((EventJump) e).setYaw(RotationUtil.yaw);
        }
		super.onEvent(e);
	}
	
}
