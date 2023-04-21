package client.rapid.command;

import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

public abstract class Command {
	protected String name, description, syntax;
	protected List<String> aliases;
	
	public Command(String name, String description, String syntax, String... aliases) {
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.aliases = Arrays.asList(aliases);
	}

	public void sendSyntax() {
		PlayerUtil.addChatMessage(EnumChatFormatting.RED + "Usage: ." + syntax + "!");
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
