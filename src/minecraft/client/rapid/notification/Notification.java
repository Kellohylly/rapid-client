package client.rapid.notification;

import client.rapid.Wrapper;
import client.rapid.util.MinecraftUtil;
import client.rapid.util.font.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.util.ResourceLocation;

public class Notification {
    private final Type type;
    private final String title, message;
    private final long fadedIn, fadeOut, end;
    private final MCFontRenderer font = Fonts.normal;

    private long start;

    public Notification(String title, String message, Type type) {
        this.type = type;
        this.title = title;
        this.message = message;
        fadedIn = 200L * 2;
        fadeOut = fadedIn + 500L * 2;
        end = fadeOut + fadedIn;
    }

    public enum Type {
        INFO, WARNING, ERROR
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public String getMessage() {
        return message;
    }

    public void render(int x, int y) {
        Minecraft mc = MinecraftUtil.mc;

        boolean mcFont = Wrapper.getSettingsManager().getSettingByName("HUD", "Minecraft Font").isEnabled();
        double offset;
        int width = mcFont ? mc.fontRendererObj.getStringWidth(message) + 35 : font.getStringWidth(message) + 35;

        if (getTime() < fadedIn)
            offset = Math.tanh(getTime() / (double) (150) * 3.0) * width;
        else
            offset = getTime() > fadeOut ? (Math.tanh(1 - (getTime() - fadeOut) / (double) (end - fadeOut) * 3.0) * width) : width;

        int color;

        if (type == Type.INFO)
            color = 0xFF001AC4;
        else
            color = type == Type.WARNING ? 0xFFCCC100 : 0xFFCC0012;

        Gui.drawRect(x - offset, y - 30, x, y - 4, 0x90000000);
        Gui.drawRect(x - offset, y - 4, x, y - 5, color);

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();

        if(type == Type.WARNING)
            mc.getTextureManager().bindTexture(new ResourceLocation("rapid/images/warning.png"));
        else
            mc.getTextureManager().bindTexture(new ResourceLocation(type == Type.ERROR ? "rapid/images/error.png" : "rapid/images/info.png"));

        Gui.drawModalRectWithCustomSizedTexture((int) (x - offset) + 6, y - 27, 0, 0, 20, 20, 20, 20);
        GlStateManager.popMatrix();

        if(mcFont) {
            mc.fontRendererObj.drawString(title, (int) (x - offset + 33), y - 27, -1);
            mc.fontRendererObj.drawString(message, (int) (x - offset + 33), y - 15, -1);
        } else {
            font.drawString(title, (int) (x - offset + 33), y - 27, -1);
            font.drawString(message, (int) (x - offset + 33), y - 15, -1);
        }
    }
}
