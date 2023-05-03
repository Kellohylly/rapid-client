package client.rapid.event.events.game;

import client.rapid.event.events.Event;

public class EventRenderWorld extends Event {
    private float partialTicks;

    public EventRenderWorld(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
