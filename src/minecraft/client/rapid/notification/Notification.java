package client.rapid.notification;

import client.rapid.Client;
import client.rapid.module.modules.hud.HudSettings;
import client.rapid.util.TimerUtil;
import client.rapid.util.animation.Animation;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Notification {

    // Details
    private final String title, message;
    private final NotificationType type;

    // Time
    private long start;
    private final int maxTime;

    // Minecraft instance
    private final Minecraft mc;

    // Utils
    private final Animation animation;
    private final Animation animationY;
    private final MCFontRenderer font;

    private final TimerUtil timer;

    public Notification(String title, String message, NotificationType type, int maxTime) {
        this.mc = Minecraft.getMinecraft();

        this.type = type;
        this.title = title;
        this.message = message;
        this.maxTime = maxTime * 1000;

        this.animation = new Animation(0, 0.6f);
        this.animationY = new Animation(0, 0.6f);
        this.font = Fonts.normal;

        this.timer = new TimerUtil();
        timer.reset();
    }

    public void render(int x, int y) {
        boolean mcFont = Client.getInstance().getSettingsManager().getSetting(HudSettings.class, "Minecraft Font").isEnabled();
        int width = mcFont ? mc.fontRendererObj.getStringWidth(message) + 35 : font.getStringWidth(message) + 35;

        Gui.drawRect(x - animation.getValue(), y - 30, x, y - 4, 0x90000000);

        double number = ((float)(maxTime / 100 - 1 - timer.time() / 100) / 10);

        String counter;

        if(number <= 0) {
            counter = "0.0";
        } else {
            counter = String.format("%.1f", number);
        }

        String text = mcFont ? title + "§7 (" + counter + ")" : title + " &7(" + counter + ")";

        // Draw title and message
        this.drawString(text, (int) (x - animation.getValue() + 33), y - 27);
        this.drawString(message, (int) (x - animation.getValue() + 33), y - 15);

        // Do animation
        if(timer.reached(maxTime)) {
            animation.interpolate(0);
        } else {
            animation.interpolate(getTime() > 0 ? width : 0);
        }

        GlStateManager.pushMatrix();

        // Improve image quality
        GlStateManager.enableBlend();

        // Draw image
        mc.getTextureManager().bindTexture(type.getImage());
        Gui.drawModalRectWithCustomSizedTexture((int) (x - animation.getValue()) + 6, y - 27, 0, 0, 20, 20, 20, 20);

        GlStateManager.popMatrix();
    }

    private void drawString(String text, float x, float y) {
        if(Client.getInstance().getSettingsManager().getSetting(HudSettings.class, "Minecraft Font").isEnabled()) {
            mc.fontRendererObj.drawString(text, x, y, -1);
            mc.fontRendererObj.drawString(text, x, y, -1);
        } else {
            font.drawString(text, x, y, -1);
            font.drawString(text, x, y, -1);
        }
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public boolean isVisible() {
        return getTime() <= maxTime + 500;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public Animation getAnimationY() {
        return animationY;
    }

    public TimerUtil getTimer() {
        return timer;
    }

}
