package client.rapid.rpc;

import client.rapid.Client;
import net.arikia.dev.drpc.*;

public class RichPresence {
    private boolean running;
    private long created = 0;

    public void init() {
        running = true;
        created = System.currentTimeMillis();

        DiscordRPC.discordInitialize("1095699324480004206", new DiscordEventHandlers.Builder().build(), true);
        new CallbackThread(running).start();
    }

    public void stop() {
        running = false;
        DiscordRPC.discordShutdown();
    }

    public void update(String first, String second) {
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder(second)
        .setBigImage("large", "Version " + Client.getInstance().getVersion())
        .setDetails(first)
        .setStartTimestamps(created)
        .build());
    }
}
