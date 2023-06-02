package client.rapid.event.events.player;

import client.rapid.event.Event;

import net.minecraft.entity.Entity;

// Hooked in EntityOtherPlayerMP.java
public class EventAttackedPlayer extends Event {

    private final Entity entity;

    public EventAttackedPlayer(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
