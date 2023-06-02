package client.rapid.rpc;

import client.rapid.Client;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRP {
    private long created = 0;

    public void startRPC() {
        created = System.currentTimeMillis();

        DiscordEventHandlers.Builder builder = new DiscordEventHandlers.Builder();

        String applicationId = "1095699324480004206";

        DiscordRPC.discordInitialize(applicationId, builder.build(), true);
        DiscordRPC.discordRunCallbacks();
    }

    public void stopRPC() {
        DiscordRPC.discordShutdown();
    }

    // Change Discord Rich Presence Details
    public void updateRPC(String first, String second) {
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder(second)
        .setBigImage("large", "Version " + Client.getInstance().getVersion())
        .setDetails(first)
        .setStartTimestamps(created)
        .build());
    }

}
