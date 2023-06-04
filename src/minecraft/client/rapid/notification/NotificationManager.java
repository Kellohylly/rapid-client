package client.rapid.notification;

import client.rapid.Client;
import client.rapid.module.modules.hud.Notifications;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    private static final LinkedBlockingQueue<Notification> queue = new LinkedBlockingQueue<>();
    private static final Notification[] notifications = new Notification[100];

    public static void add(String title, String message, NotificationType type, int time) {
        addToQueue(new Notification(title, message, type, time));
    }

    public static void addToQueue(Notification notification) {
        queue.add(notification);
    }

    public static void update() {
        int amount = (int) Client.getInstance().getSettingsManager().getSetting(Notifications.class, "Amount").getValue();

        for(int i = 0; i < amount; i++) {
            if(notifications[i] != null && !notifications[i].isVisible()) {
                notifications[i].getTimer().reset();
                notifications[i] = null;
            }

            if(notifications[i] == null && !queue.isEmpty()) {
                notifications[i] = queue.poll();
                notifications[i].getTimer().reset();
                notifications[i].start();
            }
        }
    }

    public static void render(int x, int y) {
        update();

        int i = 0;
        for(Notification notification : notifications) {
            if(notification != null) {
                notification.render(x, (y + 26) - (int)notification.getAnimationY().getValue());

                if(!notification.getTimer().reached(notification.getMaxTime() + 150)) {
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