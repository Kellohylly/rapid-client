package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.command.Command;
import client.rapid.module.modules.hud.Watermark;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;

public class WatermarkCommand extends Command {

	public WatermarkCommand() {
		super("Watermark", "Changes watermark text.", "watermark <text>", "watermark", "w");
	}

	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String text = "";
			for(String arg : args) text += arg + " ";
			
			if(Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Minecraft Font").isEnabled()) {
				text = text.replace("&", "\u00a7");
			}
			
			Watermark.setWatermark(text.replace("{version}", Client.getInstance().getVersion()));
			PlayerUtil.addChatMessage(EnumChatFormatting.GREEN + "Watermark changed!");

			if(Wrapper.getConfigManager().getHudConfig() != null)
				Wrapper.getConfigManager().getHudConfig().save();
		} else
			sendSyntax();
	}

}
