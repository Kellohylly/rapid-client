package client.rapid.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import client.rapid.command.commands.*;
import client.rapid.event.events.game.EventChat;

public class CommandManager {
	
	private final List<Command> commands = new ArrayList<>();
	public String prefix = ".";
	
	public CommandManager() {
		commands.add(new WatermarkCommand());
		commands.add(new NameCommand());
		commands.add(new ListCommand());
		commands.add(new ConfigCommand());
		commands.add(new VClipCommand());
		commands.add(new ToggleCommand());
		commands.add(new BindCommand());
	}
	
	public void handle(EventChat e) {
		String message = e.getMessage();
		
		if(!message.startsWith(prefix))
			return;
		
		e.cancel();
		
		message = message.substring(prefix.length());
		
		if(message.split(" ").length > 0) {
			String command = message.split(" ")[0];
			
			for(Command c : commands) {
				if(c.aliases.contains(command))
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
			}
		}
	}
	
	public List<Command> getCommands() {
		return commands;
	}

}
