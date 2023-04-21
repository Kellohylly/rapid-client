package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventMove;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.RotationUtil;
import net.minecraft.entity.EntityLivingBase;

@ModuleInfo(getName = "Target Strafe", getCategory = Category.COMBAT)
public class TargetStrafe extends Module {
	private final Setting
    range = new Setting("Range", this, 2, 1, 6, false),
    controllable = new Setting("Controllable", this, true),
    groundOnly = new Setting("Ground Only", this, true);

    private int direction = 1;

    public TargetStrafe() {
		add(range, controllable, groundOnly);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(mc.thePlayer.isCollidedHorizontally)
                direction = -direction;

            if(controllable.isEnabled()) {
                if (mc.gameSettings.keyBindLeft.isKeyDown())
                    direction = 1;

                if (mc.gameSettings.keyBindRight.isKeyDown())
                    direction = -1;
            }
		}
		if(e instanceof EventMove) {
			EntityLivingBase target = KillAura.target;

            if(!mc.thePlayer.onGround && groundOnly.isEnabled())
                return;

			if(target != null && !mc.gameSettings.keyBindBack.isKeyDown())
                setSpeed((EventMove)e, getMoveSpeed(), RotationUtil.getRotations(target, 0, 0)[0], direction, (mc.thePlayer.getDistanceToEntity(target) <= range.getValue()) ? 0.0 : 1.0);
		}
	}
	
	private void setSpeed(EventMove event, double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0) {
            if (strafe > 0.0)
                yaw += forward > 0.0 ? -45 : 45;
            else if (strafe < 0.0)
                yaw += forward > 0.0 ? 45 : -45;

            strafe = 0.0;
            if (forward > 0.0)
                forward = 1.0;
            else if (forward < 0.0)
                forward = -1.0;
        }
        double
        cos = Math.cos(Math.toRadians((yaw + 90.0f))),
        sin = Math.sin(Math.toRadians((yaw + 90.0f)));

        event.x = forward * moveSpeed * cos + strafe * moveSpeed * sin;
        event.z = forward * moveSpeed * sin - strafe * moveSpeed * cos;
    }
}
