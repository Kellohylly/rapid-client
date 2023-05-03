package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.command.Command;
import client.rapid.module.modules.visual.Hud;
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
			
			if(Wrapper.getSettingsManager().getSettingByName("HUD", "Minecraft Font").isEnabled()) {
				text = text.replace("&", "\u00a7");
			}
			
			Hud.setWatermark(text.replace("{version}", Client.getInstance().getVersion()));
			PlayerUtil.addChatMessage(EnumChatFormatting.GREEN + "Watermark changed!");

			if(Wrapper.getDragConfig() != null)
				Wrapper.getDragConfig().save();
		} else
			sendSyntax();
	}

}
