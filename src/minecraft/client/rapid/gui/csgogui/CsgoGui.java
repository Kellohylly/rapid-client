package client.rapid.gui.csgogui;

import client.rapid.Wrapper;
import client.rapid.gui.GuiPosition;
import client.rapid.gui.csgogui.component.Comp;
import client.rapid.gui.csgogui.component.components.*;
import client.rapid.module.Module;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.visual.Hud;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class CsgoGui extends GuiScreen {
    public double posX, posY, width2, height2, dragX, dragY;
    public boolean dragging, binding;
    public Category selectedCategory;

    private GuiPosition position;

    private Module selectedModule, bindingModule;

    public final MCFontRenderer
    font = Fonts.normal,
    icon1 = Fonts.icons4Big,
    icon2 = Fonts.icons5Big;

    int heightt = 0;

    public ArrayList<Comp> comps = new ArrayList<>();

    public static int
    rapidadapta = new Color(0xFF0F0F13).brighter().getRGB(),
    rapidadaptaDark = 0xFF0D0E11,
    nord = 0xFF3B4252,
    nordDark = 0xFF2E3440,
    background = rapidadapta,
    backgroundDark = rapidadaptaDark;

    public CsgoGui() {
        dragging = false;
        posX = 70;
        posY = 70;
        width2 = posX + 150 * 2;
        height2 = height2 + 200;
        selectedCategory = Category.COMBAT;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (dragging) {
            posX = mouseX - dragX;
            posY = mouseY - dragY;
        }
        width2 = posX + 200 * 2;
        height2 = posY + 250;

        if(Wrapper.getSettingsManager().getSettingByName("Click Gui", "Background").isEnabled())
            Gui.drawRect(0, 0, width, height, 0x84000000);

        if(Wrapper.getSettingsManager().getSettingByName("Click Gui", "Outline").isEnabled())
            Gui.drawRect(posX - 2.5, posY - 18.5, width2 + 2.5, height2 + 2.5, ((Hud)Wrapper.getModuleManager().getModule("HUD")).getColor(0));

        Gui.drawRect(posX - 2, posY - 18, width2 + 2, height2 + 2, backgroundDark);
        Gui.drawRect(posX + 252, posY, width2, height2, background);

        font.drawCenteredString("R&fapid", (float)posX + 25, (float)posY - 13, ((Hud)Wrapper.getModuleManager().getModule("HUD")).getColor(0));
        font.drawCenteredString(selectedCategory.getName(), (float)posX + 150, (float)posY - 13, -1);
        font.drawCenteredString(selectedModule != null ? selectedModule.getName() : "", (float)posX + 325, (float)posY - 13, -1);

        int i = 0;
        int wheel = Mouse.getDWheel();
        for(Category c : Category.values()) {
            int size = 50;

            Gui.drawRect(posX, posY + i, posX + size, posY + 10 + i + size - 10, c.equals(selectedCategory) ? ((Hud) Wrapper.getModuleManager().getModule("HUD")).getColor(0) : background);

            if(c == Category.PLAYER)
                icon1.drawCenteredString(String.valueOf(c.getIcon()), (float)posX + size / 2, (float)posY + i + 17, -1);
            else
                icon2.drawCenteredString(String.valueOf(c.getIcon()), (float)posX + size / 2, (float)posY + i + 17, -1);
            i+= size;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        glScissor(posX, posY, width2 - posX, height2 - posY);

        i = 0;
        for(Module m : Wrapper.getModuleManager().getModulesInCategory(selectedCategory)) {
            Gui.drawRect(posX + 52, posY + i + heightt, posX + 250, posY + i + heightt + 23, background);
            font.drawString(m.getName() + (m.getKey() == 0 ? (binding && m == bindingModule ? " &7Listening..." : "") : " &7[" + (binding && m == bindingModule ? "Listening...]" : Keyboard.getKeyName(m.getKey()) + "]")), (float)posX + 60, (float)posY + (float)heightt + 8 + i, m.isEnabled() ? ((Hud) Wrapper.getModuleManager().getModule("HUD")).getColor(i) : -1);

            if(isInside(mouseX, mouseY, posX + 52, posY, posX + 250, height2)) {
                if (wheel < 0) {
                    heightt -= 1;
                } else if (wheel > 0 && heightt != 0) {
                    heightt += 1;
                }
            }
            i += 26;
        }
        for (Comp comp : comps) {
            comp.drawScreen(mouseX, mouseY);

            if(isInside(mouseX, mouseY, posX + 252, posY, width2, height2)) {
                if (wheel < 0) {
                    comp.scrollY -= 10;
                } else if (wheel > 0 && comp.scrollY != 0) {
                    comp.scrollY += 10;
                }
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        Gui.drawRect(4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4, isInside(mouseX, mouseY, 4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4) ? ((Hud)Wrapper.getModuleManager().getModule("Hud")).getColor(0) : 0xFF0F0F0F);
        Gui.drawRect(5, height - 25, 15 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 5, 0xFF1F1F1F);
        mc.fontRendererObj.drawString("Draggable Hud", 10, height - 19, -1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if(binding) {
            bindingModule.setKey(keyCode == 211 ? 0 : keyCode);
            binding = false;
        }

        for (Comp comp : comps)
            comp.keyTyped(typedChar, keyCode);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isInside(mouseX, mouseY, posX, posY - 18, width2, posY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }

        int i = 0;
        for (Category category : Category.values()) {
            if (isInside(mouseX, mouseY, posX, posY + i, posX + 50, posY + 10 + i + 40) && mouseButton == 0) {
                if(selectedCategory != category)
                    heightt = 0;

                selectedCategory = category;
            }
            i += 50;
        }
        i = 0;
        for(Module m : Wrapper.getModuleManager().getModulesInCategory(selectedCategory)) {
            if(isInside(mouseX, mouseY, posX + 52, posY + i + heightt, posX + 250, posY + i + heightt + 23)) {
                if(isInside(mouseX, mouseY, posX + 52, posY, posX + 250, height2)) {
                    if(mouseButton == 0)
                        m.toggle();
                else if(mouseButton == 2) {
                    bindingModule = m;
                    binding = true;
                    System.out.println("nice");
                } else if(mouseButton == 1) {
                    int sOffset = 3;
                    comps.clear();
                    if (Wrapper.getSettingsManager().getSettingsByMod(m) != null)
                        for (Setting setting : Wrapper.getSettingsManager().getSettingsByMod(m)) {
                            selectedModule = m;

                            if (setting.isCheck()) {
                                comps.add(new CsgoCheckbox(275, sOffset, this, selectedModule, setting));
                                sOffset += 20;
                            }
                            if (setting.isCombo()) {
                                comps.add(new CsgoCombo(275, sOffset, this, selectedModule, setting));
                                sOffset += 20;
                            }

                            if (setting.isSlider()) {
                                comps.add(new CsgoSlider(275, sOffset, this, selectedModule, setting));
                                sOffset += 20;
                            }
                        }
                    }
                }
            }
            i += 26;
        }
        for (Comp comp : comps) {
            if(isInside(mouseX, mouseY, posX + 252, posY, width2, height2))
                comp.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if(mouseButton == 0 && isInside(mouseX, mouseY, 4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4)) {
            if(position == null)
                position = new GuiPosition();
            mc.displayGuiScreen(position);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;

        for (Comp comp : comps)
            comp.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void initGui() {
        super.initGui();
        dragging = false;
    }

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    private void glScissor(double x, double y, double width, double height) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        y += height;
        GL11.glScissor((int) ((x * mc.displayWidth) / scaledResolution.getScaledWidth()), (int) (((scaledResolution.getScaledHeight() - y) * mc.displayHeight) / scaledResolution.getScaledHeight()), (int) (width * mc.displayWidth / scaledResolution.getScaledWidth()), (int) (height * mc.displayHeight / scaledResolution.getScaledHeight()));
    }

    public static void setBackground(int background) {
        CsgoGui.background = background;
    }

    public static void setBackgroundDark(int backgroundDark) {
        CsgoGui.backgroundDark = backgroundDark;
    }
}