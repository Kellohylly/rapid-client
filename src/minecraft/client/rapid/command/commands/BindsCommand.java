package client.rapid.command.commands;

import client.rapid.Wrapper;
import client.rapid.command.Command;
import client.rapid.module.Module;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class BindsCommand extends Command {

    public BindsCommand() {
        super("Binds", "Shows all binded modules", "binds", "binds", "bindlist");
    }

    @Override
    public void onCommand(String[] args, String command) {
        PlayerUtil.addChatMessage(EnumChatFormatting.GREEN + "Current Keybinds:");
        PlayerUtil.addChatMessage(EnumChatFormatting.GRAY + "<module name> - <keybind>.");
        for(Module module : Wrapper.getModuleManager().getModules()) {
            if(module.getKey() != 0) {
                PlayerUtil.addChatMessage(EnumChatFormatting.RED + module.getName() + EnumChatFormatting.GRAY + " - " + Keyboard.getKeyName(module.getKey()) + ".");
            }
        }
    }

}
