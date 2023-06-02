package client.rapid.command.commands;

import client.rapid.Client;
import client.rapid.command.Command;
import client.rapid.module.Module;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class BindsCommand extends Command {

    public BindsCommand() {
        super("Binds", "Shows all binded modules", "binds", "binds", "bindlist");
    }

    @Override
    public void onCommand(String[] args, String command) {
        Client.getInstance().addChatMessage(EnumChatFormatting.GREEN + "Current Keybinds:");
        Client.getInstance().addChatMessage(EnumChatFormatting.GRAY + "<module name> - <keybind>.");

        for(Module module : Client.getInstance().getModuleManager().getModules()) {
            if(module.getKey() != 0) {
                Client.getInstance().addChatMessage(EnumChatFormatting.RED + module.getName() + EnumChatFormatting.GRAY + " - " + Keyboard.getKeyName(module.getKey()) + ".");
            }
        }
    }

}
