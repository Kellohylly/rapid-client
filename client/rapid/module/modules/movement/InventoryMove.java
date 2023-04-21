package client.rapid.module.modules.movement;

import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import org.lwjgl.input.Keyboard;

import client.rapid.event.events.Event;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.KeyBinding;

@ModuleInfo(getName = "Inventory Move", getCategory = Category.MOVEMENT)
public class InventoryMove extends Module {

	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
            KeyBinding[] keys = { 
            		mc.gameSettings.keyBindRight,
            		mc.gameSettings.keyBindLeft,
            		mc.gameSettings.keyBindBack,
            		mc.gameSettings.keyBindForward,
            		mc.gameSettings.keyBindJump,
            		mc.gameSettings.keyBindSprint
            };
            if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {

                KeyBinding[] keys1;

                for (int length = (keys1 = keys).length, i = 0; i < length; ++i) {
                    KeyBinding key = keys1[i];

                    key.pressed = Keyboard.isKeyDown(key.getKeyCode());
                }
            } else {
                KeyBinding[] keys2;
                for (int i2 = (keys2 = keys).length, j = 0; j < i2; ++j) {
                    KeyBinding bind = keys2[j];

                    if (!Keyboard.isKeyDown(bind.getKeyCode()))
                        KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
		}
	}
}
