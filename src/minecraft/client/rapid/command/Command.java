package client.rapid.command;

import client.rapid.Client;

import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public abstract class Command {

	protected String name;
	protected String description;
	protected String syntax;

	protected List<String> aliases;
	
	public Command(String name, String description, String syntax, String... aliases) {
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.aliases = Arrays.asList(aliases);
	}

	public void sendSyntax() {
		Client.getInstance().addChatMessage(EnumChatFormatting.RED + "Usage: ." + syntax + "!");
	}
	
	public abstract void onCommand(String[] args, String command);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSyntax() {
		return syntax;
	}

}
