package client.rapid.module.modules.combat;

import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventTick;
import client.rapid.gui.clickgui.ClickGui;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.*;
import net.minecraft.client.settings.KeyBinding;

@ModuleInfo(getName = "Auto Clicker", getCategory = Category.COMBAT)
public class AutoClicker extends Module {
	private final Setting maxCps = new Setting("Max CPS", this, 12, 1, 20, false);
	private final Setting minCps = new Setting("Min CPS", this, 12, 1, 20, false);
	private final Setting random = new Setting("Random", this, 2, 1, 6, false);
	private final Setting breakBlocks = new Setting("Break Blocks", this, false);

	private final TimerUtil timer = new TimerUtil();

	public AutoClicker() {
		add(maxCps, minCps, random, breakBlocks);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventTick && e.isPre() && Mouse.isButtonDown(0) && !(mc.currentScreen instanceof ClickGui)) {
			if(breakBlocks.isEnabled() && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				return;

			int key = mc.gameSettings.keyBindAttack.getKeyCode();

			if(timer.sleep((long)(1000 / MathUtil.randomNumber(maxCps.getValue(), minCps.getValue()) + MathUtil.randomNumber(random.getValue(), -random.getValue()))))
				KeyBinding.onTick(key);
			else
				KeyBinding.setKeyBindState(key, false);
		}
	}
}