package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.command.Command;
import client.rapid.module.Module;
import net.minecraft.util.EnumChatFormatting;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", "Toggles a module", "toggle <module>", "toggle", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 0) {
            Module module = Client.getInstance().getModuleManager().getModuleWithoutSpaces(args[0]);

            if(module != null) {
                module.toggle();

                String state = module.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled";
                Client.getInstance().addChatMessage(EnumChatFormatting.RED + module.getName() + EnumChatFormatting.GRAY + " is now " + state + EnumChatFormatting.GRAY + "!");
            } else
                Client.getInstance().addChatMessage(EnumChatFormatting.RED + "Unknown Module.");
        } else
            sendSyntax();
    }
}
