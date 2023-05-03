package client.rapid.rpc;

import net.arikia.dev.drpc.DiscordRPC;

public class CallbackThread extends Thread {
    private final boolean running;

    public CallbackThread(boolean running) {
        this.running = running;
    }

    public void run() {
        while(running)
            DiscordRPC.discordRunCallbacks();
    }
}
