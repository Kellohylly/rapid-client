package client.rapid.module.modules.hud;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventRender2D;
import client.rapid.gui.HudConfigScreen;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ModuleInfo(getName = "Notifications", getCategory = Category.HUD)
public class Notifications extends Draggable {
    private final Setting amount = new Setting("Amount", this, 3, 1, 12, true);

    public Notifications() {
        super(0, 0, 100, 34);
        setX(new ScaledResolution(mc).getScaledWidth() - this.getWidth());
        setY(new ScaledResolution(mc).getScaledHeight() - this.getHeight() - 44);
        add(amount);
    }

    public void onEvent(Event e) {
        if(e instanceof EventRender2D && e.isPre()) {
            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            glScissor(x - 200, 0, width + 200, GuiScreen.height);
            NotificationManager.render(x + width, y + height);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();

            Notification example = new Notification("Example", "Im an example!", NotificationType.INFO, 2);
            if(mc.currentScreen instanceof HudConfigScreen)
                NotificationManager.addToQueue(example);

            if(!(mc.currentScreen instanceof HudConfigScreen) && !NotificationManager.getQueue().isEmpty())
                NotificationManager.getQueue().removeIf(not -> not.getMessage().equals("Im an example!"));
        }
    }

    private void glScissor(double x, double y, double width, double height) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        y += height;
        GL11.glScissor((int) ((x * mc.displayWidth) / scaledResolution.getScaledWidth()), (int) (((scaledResolution.getScaledHeight() - y) * mc.displayHeight) / scaledResolution.getScaledHeight()), (int) (width * mc.displayWidth / scaledResolution.getScaledWidth()), (int) (height * mc.displayHeight / scaledResolution.getScaledHeight()));
    }
}
