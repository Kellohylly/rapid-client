package client.rapid.command.commands;

import client.rapid.command.Command;
import client.rapid.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class VClipCommand extends Command {

    public VClipCommand() {
        super("VClip", "Clip Vertically through blocks", "vclip <number>", "vclip");
    }

    public void onCommand(String[] args, String command) {
        try {
            Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offsetAndUpdate(0, Double.parseDouble(args[0]), 0);
        } catch(Exception e) {
            PlayerUtil.addChatMessage(EnumChatFormatting.RED + "Invalid Number!");
        }
    }
}
