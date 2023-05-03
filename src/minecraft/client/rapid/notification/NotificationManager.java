package client.rapid.notification;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    private static LinkedBlockingQueue<Notification> queue = new LinkedBlockingQueue<>();
    private static Notification currentNotification = null;

    public static void addToQueue(Notification notification) {
        queue.add(notification);
    }

    public static void update() {
        if (currentNotification != null && !currentNotification.isShown())
            currentNotification = null;

        if (currentNotification == null && !queue.isEmpty()) {
            currentNotification = queue.poll();
            currentNotification.start();
        }

    }

    public static void render(int x, int y) {
        update();

        if (currentNotification != null)
            currentNotification.render(x, y);
    }

    public static LinkedBlockingQueue<Notification> getQueue() {
        return queue;
    }

    public static Notification getCurrentNotification() {
        return currentNotification;
    }
}