package client.rapid.module.modules.hud;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.module.Draggable;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(getName = "Enabled Modules", getCategory = Category.HUD)
public class EnabledModules extends Draggable {
    private final Setting listOpacity = new Setting("List Opacity", this, 100, 0, 255, true);
    private final Setting barMode = new Setting("Bar", this, "None", "Front", "Back", "Top", "Outline");
    private final Setting lowerCase = new Setting("Lowercase", this, false);
    private final Setting modeTags = new Setting("Mode Tags", this, true);
    private final Setting flipHorizontally = new Setting("Flip Horizontally", this, false);
    private final Setting hideVisual = new Setting("Hide Visuals", this, false);

    private final Setting mcFont = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Minecraft Font");
    private final Setting fontMode = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Font");
    private final Setting shadow = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Shadow");

    public static String text = Client.getInstance().getName();

    private final FontRenderer mcfont = mc.fontRendererObj;
    private MCFontRenderer font = Fonts.normal2;

    private CopyOnWriteArrayList<Module> modules;

    public EnabledModules() {
        super(0, 10, 80, 110);
        setX(new ScaledResolution(mc).getScaledWidth() - this.getWidth() - 10);
        add(listOpacity, barMode, lowerCase, modeTags, flipHorizontally, hideVisual);
    }

    @Override
    public void onEnable() {
        switch(fontMode.getMode()) {
            case "Product Sans":
                font = Fonts.normal2;
                break;
            case "Inter":
                font = Fonts.inter;
                break;
            case "SF UI":
                font = Fonts.sfui;
                break;
            case "Greycliff":
                font = Fonts.greycliff;
                break;
        }
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender && e.isPre()) {

            if(modules == null) {
                modules = new CopyOnWriteArrayList<>(Wrapper.getModuleManager().getModules());
            }

            this.sortModules();

            int i = 0;
            for(Module m : modules) {
                if (m.isEnabled() && m.getCategory() != Category.HUD) {
                    if (hideVisual.isEnabled() && m.getCategory() == Category.VISUAL) {
                        continue;
                    }

                    String modText = this.getModuleText(m);

                    float width = flipHorizontally.isEnabled() ? x + 4 : x + this.getWidth() - this.getStringWidth(modText);

                    float xPos = flipHorizontally.isEnabled() ? x : x + this.getWidth();
                    float rectWidth = flipHorizontally.isEnabled() ? x + this.getStringWidth(modText) + 4: x + this.getWidth() - this.getStringWidth(modText) - 4;
                    float bruh = flipHorizontally.isEnabled() ? 1 : -1;
                    float animY = m.getAnimY().getValueF();

                    // Draw background
                    Gui.drawRect(xPos, y + animY, rectWidth, y + animY + 12, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());

                    // Draw back bar
                    if(barMode.getMode().equals("Back") || barMode.getMode().equals("Outline")) {
                        Gui.drawRect(xPos - bruh, y + animY, xPos, y + animY + 12, getColor(i));
                    }

                    // Draw front bar
                    if(barMode.getMode().equals("Front") || barMode.getMode().equals("Outline")) {
                        Gui.drawRect(rectWidth + bruh, y + animY, rectWidth, y + animY + 12, getColor(i));
                    }

                    // Draw top bar
                    if(barMode.getMode().equals("Top")) {
                        Gui.drawRect(xPos, y, rectWidth, y - 1, getColor(0));
                    }

                    ArrayList<Module> enabled = new ArrayList<>(modules);
                    enabled.removeIf(module -> !module.isEnabled() || (hideVisual.isEnabled() && module.getCategory() == Category.VISUAL) || module.getCategory() == Category.HUD);
                    int index = enabled.indexOf(m);

                    // Draw Bottom Bar
                    if(barMode.getMode().equals("Outline")) {
                        if (index != enabled.size() - 1) {
                            String enabledText = this.getModuleText(enabled.get(index + 1));

                            int enabledWidth = flipHorizontally.isEnabled() ? x + this.getStringWidth(enabledText) + 4 : x + this.getWidth() - this.getStringWidth(enabledText) - 4;

                            Gui.drawRect(enabledWidth, y + animY + font.getHeight() + 4, rectWidth + bruh, y + animY + font.getHeight() + 5, getColor(i));

                        } else {
                            Gui.drawRect(xPos + 1, y + animY + 12, rectWidth - 1, y + animY + 13, getColor(i));
                        }
                        Gui.drawRect(xPos + 1, y, rectWidth - 1, y - 1, getColor(0));
                    }
                    this.drawString(modText, width - 2, y + animY + 2.5f, shadow.isEnabled(), getColor(i));

                    i += 12;

                    m.getAnimY().interpolate(i - 12);
                }
            }
        }
    }

    public int getColor(long index) {
        return ((HudSettings)Wrapper.getModuleManager().getModule("Hud Settings")).getColor(index);
    }

    private void drawString(String text, float x, float y, boolean shadow, int color) {
        if(mcFont.isEnabled()) {
            if(shadow) {
                mcfont.drawStringWithShadow(text, x, y, color);
            } else {
                mcfont.drawString(text, x, y, color);
            }
        } else {
            if(shadow) {
                font.drawStringWithShadow(text, x, y, color);
            } else {
                font.drawString(text, x, y, color);
            }
        }
    }

    private int getStringWidth(String text) {
        if(mcFont.isEnabled()) {
            return mcfont.getStringWidth(text);
        }
        return font.getStringWidth(text);
    }

    private void sortModules() {
        modules.sort(Comparator.comparingInt(m -> {
            String text;

            if(modeTags.isEnabled()) {
                text = ((Module) m).getName() + ((Module)m).getTag2();
            } else {
                text = ((Module) m).getName();
            }

            if(lowerCase.isEnabled()) {
                text = text.toLowerCase();
            }

            if(mcFont.isEnabled()) {
                return mcfont.getStringWidth(text);
            }
            return font.getStringWidth(text);
        }).reversed());

    }

    private String getModuleText(Module m) {
        String modTag;

        // Setting mod tag.
        if (modeTags.isEnabled()) {
            modTag = m.getTag2();
        } else {
            modTag = "";
        }

        String modText;

        // Setting mod text
        if (mcFont.isEnabled()) {
            modText = m.getName() + modTag.replace(" ", "\u00a77 ");
        } else {
            modText = m.getName() + modTag.replace(" ", "&7 ");
        }

        if (lowerCase.isEnabled()) {
            modText = modText.toLowerCase();
        }
        return modText;
    }

}
