package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.command.Command;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;

public class NameCommand extends Command {
	private static String name = "RapidUser";
	
	public NameCommand() {
		super("Name", "Hides users username.", "name <text>", "name", "n");
	}

	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String text = "";
			for(String arg : args) text += arg + " ";
			
			name = text.replace(" ", "").replace("&", "§");

			Client.getInstance().addChatMessage(EnumChatFormatting.GREEN + "Username changed!");
		} else
			sendSyntax();
	}
	
	public static String getNewName() {
		return name;
	}

}
