package client.rapid.module.modules.hud;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.game.EventRender2D;
import client.rapid.gui.HudConfigScreen;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;

@ModuleInfo(getName = "Player Position", getCategory = Category.HUD)
public class PlayerPosition extends Draggable {
    private final Setting vertical = new Setting("Vertical", this, true);

    MCFontRenderer font = Fonts.normal2;

    public PlayerPosition() {
        super(500, 200, 60, 30);
        add(vertical);
    }

    @Override
    public void drawDummy(int mouseX, int mouseY) {
        drawString("X: 69", x, y);
        drawString("Y: 420", x, y + 10);
        drawString("Z: 21", x, y + 20);
        super.drawDummy(mouseX, mouseY);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender2D) {
            if(!(mc.currentScreen instanceof HudConfigScreen) && mc.thePlayer != null) {
                String[] dimensions = {"X", "Y", "Z"};
                int[] positions = {(int)mc.thePlayer.posX, (int)mc.thePlayer.posY, (int)mc.thePlayer.posZ};

                int offset = 0;
                String design = "";
                for(int i = 0; i < 3; i++) {
                    if(vertical.isEnabled()) {
                        design = dimensions[i] + ": ";

                        design += positions[i];

                        drawString(design, x, y + offset);
                    } else {
                        design += dimensions[i] + ": ";

                        design += positions[i] + " ";

                        drawString(design, x, y);
                    }
                    offset += 10;
                }

            }
        }
    }

    private void drawString(String string, float x, float y) {
        boolean shadow = getBoolean(HudSettings.class, "Shadow");
        boolean mcFont = getBoolean(HudSettings.class, "Minecraft Font");
        int color = ((HudSettings) Client.getInstance().getModuleManager().getModule(HudSettings.class)).getColor((long) (y));
        if(mcFont) {
            if(shadow) {
                mc.fontRendererObj.drawStringWithShadow(string, x, y, color);
            } else {
                mc.fontRendererObj.drawString(string, x, y, color);
            }
        } else {
            if(shadow) {
                font.drawStringWithShadow(string, x, y, color);
            } else {
                font.drawString(string, x, y, color);
            }
        }
    }
}
