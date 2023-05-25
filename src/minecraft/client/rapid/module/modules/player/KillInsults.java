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
	private final String[] insults = new String[255];

	public KillInsults() {
		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(mc.mcDataDir + File.separator + "Rapid" + File.separator + "insults.txt"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int i = 0;
		if(scanner != null) {
			while(scanner.hasNextLine()) {
				insults[i] = scanner.nextLine();
				i++;
			}
		}
	}

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
					"wurde von " + name,
					name + " killed ",
					"killed by " + name,
					"slain by " + name,
					"You received a reward for killing ",
					"while escaping " + name,
			};

			if(unformattedText == null) {
				return;
			}

			for(String s : look) {
				if (unformattedText.contains(s)) {
					mc.thePlayer.sendChatMessage(insults[(int) MathUtil.randomNumber(insults.length - 1, 0)].replace("{name}", (((EventAttackedPlayer) e).getEntity().getName())));
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
