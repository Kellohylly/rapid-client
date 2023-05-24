package client.rapid.event.events.player;

import client.rapid.event.events.Event;
import net.minecraft.entity.Entity;

// WHERE IN THE FLYING FUCK DO I HOOK THIS??
public class EventAttackedPlayer extends Event {
    private final Entity entity;

    public EventAttackedPlayer(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
