package client.rapid.command.commands;

import java.io.File;
import java.util.StringJoiner;

import client.rapid.Client;
import org.apache.commons.io.FilenameUtils;

import client.rapid.command.Command;
import client.rapid.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class ConfigCommand extends Command {
	String dir = Minecraft.getMinecraft().mcDataDir + File.separator + "Rapid" + File.separator + "Configs";

	public ConfigCommand() {
		super("Config", "Manage configs", "config", "config", "c");
	}

	public void onCommand(String[] args, String command) {
		String[] configs = new File(dir).list();
		if(args.length > 1) {
			if(args[0].equalsIgnoreCase("load")) {
				
				StringJoiner configName = new StringJoiner(" ");

				for(int i = 1; i < args.length; i++)
					configName.add(args[i]);
				
				if(new File(dir + File.separator + configName + ".conf").exists()) {
					Client.getInstance().getConfigManager().getModuleConfig().load(new File(dir + File.separator + configName + ".conf"));
					Client.getInstance().addChatMessage(EnumChatFormatting.GREEN + "Loaded config!");
				}
				else {
					Client.getInstance().addChatMessage(EnumChatFormatting.RED + "Couldnt load config, check if the names correct,");
					Client.getInstance().addChatMessage(EnumChatFormatting.RED + "If it still doesnt work, try renaming the file to something else!");
				}
			}
		} else {
			try {
				Client.getInstance().addChatMessage(EnumChatFormatting.GREEN + "Available Configs:");

				assert configs != null;

				for(String f : configs)
					Client.getInstance().addChatMessage(FilenameUtils.removeExtension(f));
				
			} catch(Exception e) {
				Client.getInstance().addChatMessage(EnumChatFormatting.RED + "Please make a file in .minecraft/Rapid called \"Configs\"!");
			}
		}

	}

}
