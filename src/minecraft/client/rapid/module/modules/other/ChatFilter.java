package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventChat;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Chat Filter", getCategory = Category.OTHER)
public class ChatFilter extends Module {
    private final Setting antiObfuscation = new Setting("Anti Obfuscation", this, true);
    private final Setting antiFurry = new Setting("Anti Furry", this, false);
    private final Setting antiTrash = new Setting("Anti Trash", this, false);

    public ChatFilter() {
        add(antiObfuscation, antiFurry, antiTrash);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventChat && e.isPre()) {
            EventChat event = (EventChat)e;

            String[] obfCode = {"&k", "\u00a7k"};
            for(String o : obfCode) {
                if(event.getMessage().contains(o) && antiObfuscation.isEnabled())
                    event.setMessage(event.getMessage().replace(o, ""));

            }

            String censored = "[C]";

            String[] furry = {"uwu", "owo", "UWU", "OWO", "Uwu", "Owo"};
            for(String o : furry) {
                if (event.getMessage().contains(o) && antiFurry.isEnabled())
                    event.setMessage(event.getMessage().replace(o, censored));
            }

            // no brain moment.
            String[] antiL = {"l", "L", " l", " L", "l ", "L ", "trash", "Trash", "bad", "Bad", "bad client", "bad cheat"};
            for(String l : antiL) {
                if((event.getMessage().startsWith(l) || event.getMessage().endsWith(l) || event.getMessage().equals(l)) && antiTrash.isEnabled())
                    event.setMessage(event.getMessage().replace(l, censored));
            }

        }
    }
}
