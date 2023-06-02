package client.rapid.module.modules.combat;

import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(getName = "W Tap", getCategory = Category.COMBAT)
public class WTap extends Module {

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			boolean clicked = mc.gameSettings.keyBindAttack.isPressed();

			if(mc.pointedEntity != null && mc.pointedEntity instanceof EntityPlayer) {
				EntityPlayer entity = (EntityPlayer) mc.pointedEntity;

				if (entity.hurtTime == 0 && entity.isEntityAlive() && clicked) {
					mc.thePlayer.motionX *= 0;
					mc.thePlayer.motionZ *= 0;
					mc.thePlayer.moveForward = 0;

					mc.thePlayer.setSprinting(false);
				}
			}
		}
	}
}
