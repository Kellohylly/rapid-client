package client.rapid.module.modules.hud;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.game.EventRender;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(getName = "Session Info", getCategory = Category.HUD)
public class SessionInfo extends Draggable {
    float range = 0.00F;
    MCFontRenderer font = Fonts.normal2;

    public SessionInfo() {
        super(300, 400, 140, 65);
    }

    @Override
    public void drawDummy(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        mc.fontRendererObj.drawString(name, x + (float) width / 2 - (float) mc.fontRendererObj.getStringWidth(name) / 2, y + (float) height / 2 - (float) mc.fontRendererObj.FONT_HEIGHT / 2, -1);
        super.drawDummy(mouseX, mouseY);
    }

    @Override
    public void onEvent(Event e) {
        int kills = 0;
        if(e instanceof EventPacket && e.isPre()) {
            if (((EventPacket)e).getPacket() instanceof S02PacketChat) {

                String unformattedText = ((S02PacketChat) ((EventPacket)e).getPacket()).getChatComponent().getUnformattedText();

                String name = mc.thePlayer.getName();
                String[] look = {
                        "killed by " + name,
                        "slain by " + name,
                        "You received a reward for killing ",
                        "while escaping " + name,
                };
                for(String s : look) {
                    if (unformattedText.contains(s))
                        kills++;
                }
            }
        }
        if (e instanceof EventRender && e.isPre()) {
            Gui.drawRect(x, y, x + width, y + height, 0x90000000);
            Gui.drawRect(x, y, x + width, y + 1, ((HudSettings)Wrapper.getModuleManager().getModule("Hud Settings")).getColor(0));

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5, 1.5, 1.5);
            mc.fontRendererObj.drawStringWithShadow("Session Info", (x / 1.5f) + 2, (y / 1.5f) + 3, -1);
            GlStateManager.popMatrix();

            mc.fontRendererObj.drawStringWithShadow("Kills: " + kills, x + 3, y + height - 20, -1);
            mc.fontRendererObj.drawStringWithShadow("Health: " + mc.thePlayer.getHealth(), x + 3, y + height - 10, -1);
        }
    }
    public float getSpeedf() {
        return (float) Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }
    public float round(float value, int decimalPoints) {
        return (float) (Math.round(value * Math.pow(10.0F, decimalPoints)) / Math.pow(10.0F, decimalPoints));
    }
}
