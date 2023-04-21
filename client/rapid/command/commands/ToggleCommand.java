package client.rapid.command.commands;

import client.rapid.Wrapper;
import client.rapid.command.Command;
import client.rapid.module.Module;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", "Toggles a module", "toggle <module>", "toggle", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 0) {
            Module module = Wrapper.getModuleManager().getModule2(args[0]);

            if(module != null) {
                module.toggle();

                String state = module.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled";
                PlayerUtil.addChatMessage(EnumChatFormatting.RED + module.getName() + EnumChatFormatting.GRAY + " is now " + state + EnumChatFormatting.GRAY + "!");
            } else
                PlayerUtil.addChatMessage(EnumChatFormatting.RED + "Unknown Module.");
        } else
            sendSyntax();
    }
}
