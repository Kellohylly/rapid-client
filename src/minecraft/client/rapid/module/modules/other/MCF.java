package client.rapid.module.modules.other;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(getName = "MCF", getCategory = Category.OTHER)
public class MCF extends Module {
    private static final CopyOnWriteArrayList<EntityPlayer> friends = new CopyOnWriteArrayList<>();

    private boolean clicked;

    @Override
    public void onEnable() {
        friends.clear();
        this.clicked = false;
    }

    @Override
    public void onDisable() {
        friends.clear();
        this.clicked = false;
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            if(Mouse.isButtonDown(2)) {
                if (mc.pointedEntity != null && !clicked) {
                    if (mc.pointedEntity instanceof EntityPlayer) {

                        String message;

                        if (friends.contains(mc.pointedEntity)) {
                            friends.remove((EntityPlayer) mc.pointedEntity);
                            message = "§c" + mc.pointedEntity.getName() + " is no longer a friend!";

                        } else {
                            friends.add((EntityPlayer) mc.pointedEntity);
                            message = "§a" + mc.pointedEntity.getName() + " is now a friend!";
                        }

                        Client.getInstance().addChatMessage(message);
                    }

                    clicked = true;

                }
            } else {
                clicked = false;

            }
        }
    }

    public static CopyOnWriteArrayList<EntityPlayer> getFriends() {
        return friends;
    }

}
