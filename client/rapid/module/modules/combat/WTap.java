package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "W Tap", getCategory = Category.COMBAT)
public class WTap extends Module {
	
	public void onEvent(Event e) {
		boolean clicked = mc.gameSettings.keyBindAttack.isKeyDown();

		if(e instanceof EventUpdate && e.isPre() && mc.pointedEntity != null && !mc.pointedEntity.isDead && !mc.pointedEntity.velocityChanged && mc.gameSettings.keyBindForward.isKeyDown() && clicked && mc.pointedEntity.hurtResistantTime < 12 && mc.thePlayer.isSprinting()) {
			mc.thePlayer.motionX *= 0;
			mc.thePlayer.motionZ *= 0;
			mc.thePlayer.moveForward = 0;

			mc.thePlayer.setSprinting(false);
			mc.thePlayer.setSprinting(true);
		}
	}
}
