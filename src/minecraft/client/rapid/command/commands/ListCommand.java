package client.rapid.command.commands;

import client.rapid.Wrapper;
import client.rapid.command.Command;
import client.rapid.util.PlayerUtil;

public class ListCommand extends Command {

	public ListCommand() {
		super("List", "Shows all commands.", "list", "l", "list", "help", "commands");
	}

	public void onCommand(String[] args, String command) {
		for(Command c : Wrapper.getCommandManager().getCommands()) {
			PlayerUtil.addChatMessage("§a" + c.getSyntax() + " §7- " + c.getDescription());
		}
	}

}
