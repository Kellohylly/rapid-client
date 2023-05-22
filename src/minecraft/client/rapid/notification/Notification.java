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

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class Notification {
    private final NotificationType type;
    private final String title, message;
    private final MCFontRenderer font = Fonts.normal;

    public final TimerUtil timer = new TimerUtil();

    private long start;
    private int seconds;

    private final Animation animation = new Animation(1, 0.25f);

    public Notification(String title, String message, NotificationType type, int seconds) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.seconds = seconds * 1000;
        timer.reset();
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= seconds + 500;
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

        double number = ((float)(seconds / 100 - 1 - timer.time() / 100) / 10) - 0.6;
        String counter = number <= 0 ? "0.0" : String.format("%.1f", number);

        if(mcFont) {
            mc.fontRendererObj.drawString(title + EnumChatFormatting.GRAY + " (" + counter + ")", (int) (x - animation.getValue() + 33), y - 27, -1);
            mc.fontRendererObj.drawString(message, (int) (x - animation.getValue() + 33), y - 15, -1);
        } else {
            font.drawString(title + " &7(" + counter + ")", (int) (x - animation.getValue() + 33), y - 27, -1);
            font.drawString(message, (int) (x - animation.getValue() + 33), y - 15, -1);
        }

        if(timer.reached(seconds - 1000 + 450)) {
            animation.interpolate(0);
        } else {
            animation.interpolate(getTime() > 0 ? width : 0);
        }

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();

        ResourceLocation image;

        if(type == NotificationType.WARNING)
            image = new ResourceLocation("rapid/images/warning.png");
        else if(type == NotificationType.ERROR)
            image = new ResourceLocation("rapid/images/error.png");
        else
            image = new ResourceLocation("rapid/images/info.png");

        mc.getTextureManager().bindTexture(image);

        Gui.drawModalRectWithCustomSizedTexture((int) (x - animation.getValue()) + 6, y - 27, 0, 0, 20, 20, 20, 20);
        GlStateManager.popMatrix();

    }
}
