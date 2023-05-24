package client.rapid.module.modules.player;

import java.io.File;
import java.util.Scanner;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventAttackedPlayer;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.MathUtil;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(getName = "Insults", getCategory = Category.PLAYER)
public class KillInsults extends Module {
	private String unformattedText;

	@Override
	public void onEnable() {
		unformattedText = "";
	}

	@Override
	public void onDisable() {
		unformattedText = "";
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventAttackedPlayer && e.isPre()) {
			String name = mc.thePlayer.getName();

			String[] look = {
					"killed by " + name,
					"slain by " + name,
					"You received a reward for killing ",
					"while escaping " + name,
			};

			if(unformattedText == null) {
				return;
			}

			String[] str = new String[255];
			for(String s : look) {
				if (unformattedText.contains(s)) {
					Scanner scanner = null;

					try {
						scanner = new Scanner(new File(mc.mcDataDir + File.separator + "Rapid" + File.separator + "insults.txt"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					int i = 0;
					if(scanner != null) {
						while(scanner.hasNextLine()) {
							str[i] = scanner.nextLine().replace("{name}", (((EventAttackedPlayer) e).getEntity().getName()));
							i++;
						}
						mc.thePlayer.sendChatMessage(str[(int) MathUtil.randomNumber(i, 0)]);
					}
				}
			}
		}
		if(e instanceof EventPacket && e.isPre()) {
			if (((EventPacket)e).getPacket() instanceof S02PacketChat) {
				this.unformattedText = ((S02PacketChat) ((EventPacket)e).getPacket()).getChatComponent().getUnformattedText();
			}
		}
	}
}
