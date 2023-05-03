package client.rapid.command.commands;

import client.rapid.Wrapper;
import client.rapid.command.Command;
import client.rapid.module.Module;
import client.rapid.util.PlayerUtil;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "Binds a module", "bind <module> <key>", "bind", "b");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 1) {
            Module module = Wrapper.getModuleManager().getModule2(args[0]);

            if(module != null) {
                module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));

                PlayerUtil.addChatMessage(EnumChatFormatting.RED + module.getName() + EnumChatFormatting.GRAY + " now binded to " + EnumChatFormatting.RED + Keyboard.getKeyName(module.getKey()) + EnumChatFormatting.GRAY + "!");
            } else
                PlayerUtil.addChatMessage(EnumChatFormatting.RED + "Unknown Module.");
        } else
            sendSyntax();
    }
}
