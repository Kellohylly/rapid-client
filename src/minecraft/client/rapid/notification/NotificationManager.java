package client.rapid.notification;

import client.rapid.Client;
import client.rapid.module.modules.hud.Notifications;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    private static final LinkedBlockingQueue<Notification> queue = new LinkedBlockingQueue<>();
    private static final Notification[] nots = new Notification[100];

    public static void add(String title, String message, NotificationType type, int time) {
        addToQueue(new Notification(title, message, type, time));
    }

    public static void addToQueue(Notification notification) {
        queue.add(notification);
    }

    public static void update() {
        for(int i = 0; i < Client.getInstance().getSettingsManager().getSetting(Notifications.class, "Amount").getValue(); i++) {
            if(nots[i] != null && !nots[i].isShown()) {
                nots[i].timer.reset();
                nots[i] = null;
            }

            if(nots[i] == null && !queue.isEmpty()) {
                nots[i] = queue.poll();
                nots[i].start();
                nots[i].timer.reset();
            }
        }
    }

    public static void render(int x, int y) {
        update();

        int i = 0;
        for(Notification notification : nots) {
            if(notification != null) {
                notification.render(x, (y + 26) - (int)notification.getAnimationY().getValue());

                if(!notification.timer.reached(notification.getMaxTime() + 150)) {
                    i += 26;
                    notification.getAnimationY().interpolate(i);
                }
            }
        }
    }

    public static LinkedBlockingQueue<Notification> getQueue() {
        return queue;
    }

}