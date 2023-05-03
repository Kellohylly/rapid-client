package client.rapid.module.modules.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.util.Logger;
import org.apache.commons.lang3.RandomStringUtils;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.MathUtil;
import client.rapid.util.TimerUtil;

@ModuleInfo(getName = "Spammer", getCategory = Category.OTHER)
public class Spammer extends Module {
	private final Setting
	delay = new Setting("Delay", this, 100, 10, 1000, true),
	randomLength = new Setting("Random Length", this, 6, 4, 12, true);

	private final TimerUtil timer = new TimerUtil();

	public Spammer() {
		add(delay, randomLength);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventPacket && e.isPre()) {
			Scanner scanner;

			try {
				scanner = new Scanner(new File(mc.mcDataDir + File.separator + "Rapid" + File.separator + "spammer.txt"));
				
				String[] text = new String[255];
				int i = 0;
				while(scanner.hasNextLine()) {
					text[i] = scanner.nextLine();
					i++;
				}

				if(timer.sleep((long)delay.getValue() * 5))
					mc.thePlayer.sendChatMessage(text[(int) MathUtil.randomNumber(i, 0)] + " [" + RandomStringUtils.random((int)randomLength.getValue(), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789") + "]");
				
			} catch (FileNotFoundException ex) {
				Logger.error("Something went wrong: " + ex.getCause());
			}
		}
	}
}
