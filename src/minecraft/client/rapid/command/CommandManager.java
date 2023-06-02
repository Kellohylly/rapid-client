package client.rapid.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import client.rapid.command.commands.*;
import client.rapid.event.events.game.EventChat;

public class CommandManager {
	
	private final List<Command> commands = new ArrayList<>();

	public CommandManager() {
		this.register(
			WatermarkCommand.class,
			NameCommand.class,
			ListCommand.class,
			ConfigCommand.class,
			VClipCommand.class,
			ToggleCommand.class,
			BindCommand.class,
			BindsCommand.class
		);
	}

	// Adds a bunch of commands to commands array
	@SafeVarargs
	public final void register(Class<? extends Command>... commandClasses) {
		try {
			for(Class<? extends Command> command : commandClasses) {
				commands.add(command.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Handles commands
	public void handleChat(EventChat e) {
		String message = e.getMessage();
		
		if(!message.startsWith(".")) {
			return;
		}
		
		e.cancel();
		
		message = message.substring(".".length());
		
		if(message.split(" ").length > 0) {
			String command = message.split(" ")[0];
			
			for(Command c : commands) {
				if(c.aliases.contains(command)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
				}
			}
		}
	}
	
	public List<Command> getCommands() {
		return commands;
	}

}
