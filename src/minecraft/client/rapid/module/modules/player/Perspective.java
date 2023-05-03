package client.rapid.module.modules.player;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventJump;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventStrafe;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "Perspective", getCategory = Category.PLAYER)
public class Perspective extends Module {
    public float[] rotations = new float[2];

    @Override
    public void onEnable() {
        mc.gameSettings.thirdPersonView = 1;
        rotations[0] = mc.thePlayer.rotationYaw;
        rotations[1] = mc.thePlayer.rotationPitch;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.thirdPersonView = 0;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            ((EventMotion) e).setYaw(rotations[0]);
            ((EventMotion) e).setPitch(rotations[1]);
            mc.thePlayer.rotationYawHead = rotations[0];
            mc.thePlayer.renderYawOffset = rotations[0];
            mc.thePlayer.rotationPitchHead = rotations[1];
        }

        if (e instanceof EventStrafe)
            ((EventStrafe) e).setYaw(rotations[0]);

        if (e instanceof EventJump)
            ((EventJump) e).setYaw(rotations[0]);
    }
}