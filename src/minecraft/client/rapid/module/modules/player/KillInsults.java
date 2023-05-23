package client.rapid.module.modules.player;

import java.io.File;
import java.util.Scanner;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventKilledPlayer;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.TimerUtil;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(getName = "Insults", getCategory = Category.PLAYER)
public class KillInsults extends Module {
	private final TimerUtil timer = new TimerUtil();

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventKilledPlayer && e.isPre()) {
			EventKilledPlayer event = (EventKilledPlayer) e;
			if(event.getEntity().getName().equals(mc.thePlayer.getName())) {
				System.out.println("RIP " + ((EventKilledPlayer) e).getEntity().getName());
			}
			System.out.println(event.getEntity().getName());
		}
		if(e instanceof EventPacket && e.isPre()) {
			if (((EventPacket)e).getPacket() instanceof S02PacketChat) {

				String name = mc.thePlayer.getName();

				String[] look = {
					"killed by " + name,
					"slain by " + name,
					"You received a reward for killing ",
					"while escaping " + name,
				};

				String unformattedText = ((S02PacketChat) ((EventPacket)e).getPacket()).getChatComponent().getUnformattedText();

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
								str[i] = scanner.nextLine();
								i++;
							}
							
							// goofy ahh moment
							/*if(timer.sleep(100))
								mc.thePlayer.sendChatMessage(str[(int) MathUtil.randomNumber(i, 0)]);*/
						}
					}
				}
			}
		}
	}
}
