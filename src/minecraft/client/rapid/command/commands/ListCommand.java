package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.command.Command;

public class ListCommand extends Command {

	public ListCommand() {
		super("List", "Shows all commands.", "list", "l", "list", "help", "commands");
	}

	public void onCommand(String[] args, String command) {
		for(Command c : Client.getInstance().getCommandManager().getCommands()) {
			Client.getInstance().addChatMessage("§a" + c.getSyntax() + " §7- " + c.getDescription());
		}
	}

}
