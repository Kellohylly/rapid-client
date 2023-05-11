package client.rapid.notification;

import client.rapid.Wrapper;
import client.rapid.util.MinecraftUtil;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.animation.Animation;
import client.rapid.util.font.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.util.ResourceLocation;

public class Notification {
    private final Type type;
    private final String title, message;
    private final MCFontRenderer font = Fonts.normal;

    public final TimerUtil timer = new TimerUtil();

    private long start;

    private final Animation animation = new Animation(1, 0.25f);

    public Notification(String title, String message, Type type) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public enum Type {
        INFO, WARNING, ERROR
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= 3500;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public String getMessage() {
        return message;
    }

    public void render(int x, int y) {
        Minecraft mc = MinecraftUtil.mc;

        boolean mcFont = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Minecraft Font").isEnabled();
        int width = mcFont ? mc.fontRendererObj.getStringWidth(message) + 35 : font.getStringWidth(message) + 35;

        Gui.drawRect(x - animation.getValue(), y - 30, x, y - 4, 0x90000000);

        if(mcFont) {
            mc.fontRendererObj.drawString(title, (int) (x - animation.getValue() + 33), y - 27, -1);
            mc.fontRendererObj.drawString(message, (int) (x - animation.getValue() + 33), y - 15, -1);
        } else {
            font.drawString(title, (int) (x - animation.getValue() + 33), y - 27, -1);
            font.drawString(message, (int) (x - animation.getValue() + 33), y - 15, -1);
        }

        if(timer.reached(2450)) {
            animation.interpolate(0);
        } else {
            animation.interpolate(getTime() > 0 ? width : 0);
        }

        int color = 0xFFCC0012;

        if (type == Type.INFO)
            color = 0xFF001AC4;
        else if (type == Type.WARNING)
            color = 0xFFCCC100;

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();

        if(type == Type.WARNING)
            mc.getTextureManager().bindTexture(new ResourceLocation("rapid/images/warning.png"));
        else
            mc.getTextureManager().bindTexture(new ResourceLocation(type == Type.ERROR ? "rapid/images/error.png" : "rapid/images/info.png"));

        Gui.drawModalRectWithCustomSizedTexture((int) (x - animation.getValue()) + 6, y - 27, 0, 0, 20, 20, 20, 20);
        GlStateManager.popMatrix();

    }
}
