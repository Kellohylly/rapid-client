package client.rapid.event.events.player;

import client.rapid.event.events.Event;
import net.minecraft.entity.Entity;

// WHERE IN THE FLYING FUCK DO I HOOK THIS??
public class EventKilledPlayer extends Event {
    private final Entity entity;

    public EventKilledPlayer(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
