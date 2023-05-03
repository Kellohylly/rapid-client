package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventMotion;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.settings.Setting;
import client.rapid.util.module.RotationUtil;

@ModuleInfo(getName = "Spin Bot", getCategory = Category.PLAYER)
public class SpinBot extends Module {
    private final Setting speed = new Setting("Rotation Speed", this, 2, 1, 5, false);
    private final Setting lookUp = new Setting("Look Up", this, true);
    private final Setting killAuraCheck = new Setting("Kill Aura Check", this, true);
    private final Setting scaffoldCheck = new Setting("Scaffold Check", this, true);

    public SpinBot() {
        add(speed, lookUp, killAuraCheck, scaffoldCheck);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion)e;

            if((killAuraCheck.isEnabled() && KillAura.target != null) || (scaffoldCheck.isEnabled() && isEnabled("Scaffold")))
                return;

            float spinYaw = RotationUtil.updateRotation(mc.thePlayer.rotationYaw, (System.currentTimeMillis() / (int)speed.getValue()) % 360);

            event.setYaw(spinYaw);

            if(lookUp.isEnabled()) {
                event.setPitch(-90);
                mc.thePlayer.rotationPitchHead = event.pitch;
            }

            mc.thePlayer.rotationYawHead = spinYaw;
            mc.thePlayer.renderYawOffset = spinYaw;

        }
    }
}
