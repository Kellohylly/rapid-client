package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.command.Command;
import client.rapid.module.modules.hud.HudSettings;
import client.rapid.module.modules.hud.Watermark;

import net.minecraft.util.EnumChatFormatting;

public class WatermarkCommand extends Command {

	public WatermarkCommand() {
		super("Watermark", "Changes watermark text.", "watermark <text>", "watermark", "w");
	}

	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String text = "";

			int i = 0;
			for(String arg : args) {
				text += (i == 0 ? "" : " ") + arg;
				i++;
			}
			
			if(Client.getInstance().getSettingsManager().getSetting(HudSettings.class, "Minecraft Font").isEnabled()) {
				text = text.replace("&", "§");
			}
			
			Watermark.setWatermark(text.replace("{version}", Client.getInstance().getVersion()));
			Client.getInstance().addChatMessage(EnumChatFormatting.GREEN + "Watermark changed!");

			if(Client.getInstance().getConfigManager().getHudConfig() != null) {
				Client.getInstance().getConfigManager().getHudConfig().save();
			}
		} else {
			sendSyntax();
		}
	}

}
