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
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(getName = "Enabled Modules", getCategory = Category.HUD)
public class EnabledModules extends Draggable {
    private final Setting listBarMode = new Setting("Bars", this, "None", "Front", "Back", "Back2", "Top", "Outline");
    private final Setting listOpacity = new Setting("List Opacity", this, 100, 0, 255, true);
    private final Setting listHeight = new Setting("List Height", this, 6, 0, 16, true);
    private final Setting lowercased = new Setting("Lowercased", this, false);
    private final Setting modeTags = new Setting("Mode Tags", this, true);
    private final Setting flipHorizontally = new Setting("Flip Horizontally", this, false);
    /*flipVertically = new Setting("Flip Vertically", this, false),*/
    private final Setting hideVisual = new Setting("Hide Visuals", this, false);
    private final Setting flipTags = new Setting("Flip Mode Tags", this, false);

    private final Setting mcFont = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Minecraft Font");
    private final Setting fontMode = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Font");
    private final Setting shadow = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Shadow");

    public static String text = Client.getInstance().getName();
    MCFontRenderer font = Fonts.normal2;

    private CopyOnWriteArrayList<Module> modules;

    public EnabledModules() {
        super(100, 100, 50, 110);
        add(listBarMode, listOpacity, listHeight, lowercased, modeTags, flipHorizontally, /*flipVertically,*/ hideVisual, flipTags);
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
        }
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender && e.isPre()) {
            int i = 0, i2 = 0, height = (int)listHeight.getValue();

            if(modules == null)
                modules = new CopyOnWriteArrayList<>(Wrapper.getModuleManager().getModules());

            int width = getWidth() / 2;

            modules.sort(Comparator.comparingInt(mod -> lowercased.isEnabled() ? (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth((((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : "")).toLowerCase()) : font.getStringWidth((((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : "")).toLowerCase())) : mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : "")) : font.getStringWidth(((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : ""))).reversed());

            for(Module m : modules) {
                if(m.isEnabled() && m.getCategory() != Category.HUD) {
                    if(hideVisual.isEnabled() && m.getCategory() == Category.VISUAL)
                        continue;
                    String sep = (modeTags.isEnabled() ? (m.getTag() != "" ? " ": "") + m.getTag() : "");

                    String modText = m.getName() + (!mcFont.isEnabled() ? "&7" : "\u00a77") + sep;

                    if(flipTags.isEnabled())
                        modText = (!mcFont.isEnabled() ? "&7" : "\u00a77") + (modeTags.isEnabled() ? m.getTag() + (m.getTag() != "" ? " ": "") : "") + (!mcFont.isEnabled() ? "&r" : "\u00a7r") + m.getName() + (!mcFont.isEnabled() ? "&f" : "\u00a7r");


                    if(lowercased.isEnabled())
                        modText = modText.toLowerCase();

                    if(flipHorizontally.isEnabled()) {
                        if (mcFont.isEnabled()) {
                            Gui.drawRect(x + width, y + i, x + width + mc.fontRendererObj.getStringWidth(modText) + 4, y + 9 + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
                            if(shadow.isEnabled())
                                mc.fontRendererObj.drawStringWithShadow(modText, x + width + 2, y + i + (float) height / 2 + 1, getColor(i2));
                            else
                                mc.fontRendererObj.drawString(modText, x + width + 2, y + i + (float) height / 2 + 1, getColor(i2));

                        } else {
                            Gui.drawRect(x + width, y + i, x + width + font.getStringWidth(modText) + 4, 9 + y + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
                            if(shadow.isEnabled())
                                font.drawStringWithShadow(modText, x + width + 2, y + i + (float) height / 2 + 1, getColor(i2));
                            else
                                font.drawString(modText, x + width + 2, y + i + (float) height / 2 + 1, getColor(i2));
                        }
                    } else {
                        if (mcFont.isEnabled()) {
                            Gui.drawRect(x + width + 1, y + i, x + width - mc.fontRendererObj.getStringWidth(modText) - 5, y + 9 + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
                            if(shadow.isEnabled())
                                mc.fontRendererObj.drawStringWithShadow(modText, x + width - mc.fontRendererObj.getStringWidth(modText) - 2, y + i + (float) height / 2 + 1, getColor(i2));
                            else
                                mc.fontRendererObj.drawString(modText, x + width - mc.fontRendererObj.getStringWidth(modText) - 2, y + i + (float) height / 2 + 1, getColor(i2));
                        } else {
                            Gui.drawRect(x + width + 1, y + i, x + width - font.getStringWidth(modText) - 3, 9 + y + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
                            if(shadow.isEnabled())
                                font.drawStringWithShadow(modText, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) : font.getStringWidth(modText)) - 1, y + i + (float) height / 2 + 1, getColor(i2));
                            else
                                font.drawString(modText, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) : font.getStringWidth(modText)) - 1, y + i + (float) height / 2 + 1, getColor(i2));

                        }
                    }

                    switch (listBarMode.getMode()) {
                        case "Front":
                            if(flipHorizontally.isEnabled())
                                Gui.drawRect(x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 4), y + i, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 5), y + 9 + i + height, getColor(i2));
                            else
                                Gui.drawRect(x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y + i, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + 9 + i + height, getColor(i2));
                            break;
                        case "Back":
                            if(flipHorizontally.isEnabled())
                                Gui.drawRect(x + width - 1, y + i, x + width, 9 + y + i + height, getColor(i2));
                            else
                                Gui.drawRect(x + width + 1, y + i, x + width + 2, 9 + y + i + height, getColor(i2));
                            break;
                        case "Back2":
                            if(flipHorizontally.isEnabled())
                                Gui.drawRect(x + width - 1, y + i + 1, x + width, 8 + y + i + height, getColor(i2));
                            else
                                Gui.drawRect(x + width + 1, y + i + 1, x + width + 2, 8 + y + i + height, getColor(i2));
                            break;
                        case "Top":
                            if(!flipHorizontally.isEnabled())
                                Gui.drawRect(x + width + 1, y, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y - 1, getColor(0));
                            else
                                Gui.drawRect(x + width, y, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + 1, getColor(0));
                            break;
                        case "Outline":
                            // Front
                            if(flipHorizontally.isEnabled())
                                Gui.drawRect(x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 4), y + i, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 5), y + 10 + i + height, getColor(i2));
                            else
                                Gui.drawRect(x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y + i, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + 10 + i + height, getColor(i2));

                            // Back
                            if(flipHorizontally.isEnabled())
                                Gui.drawRect(x + width - 1, y + i, x + width, 9 + y + i + height, getColor(i2));
                            else
                                Gui.drawRect(x + width + 1, y + i - 1, x + width + 2, 10 + y + i + height, getColor(i2));

                            // Top
                            if(!flipHorizontally.isEnabled())
                                Gui.drawRect(x + width + 1, y, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y - 1, getColor(0));
                            else
                                Gui.drawRect(x + width - 1, y, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + 1, getColor(0));

                            // Bottom thing.
                            ArrayList<Module> enabled = new ArrayList<>(modules);
                            enabled.removeIf(module -> !module.isEnabled() || (hideVisual.isEnabled() && module.getCategory() == Category.VISUAL) || module.getCategory() == Category.HUD);
                            int index = enabled.indexOf(m);

                            if(index != enabled.size() - 1) {
                                String sep2 = (modeTags.isEnabled() ? (enabled.get(index + 1).getTag() != "" ? " ": "") + enabled.get(index + 1).getTag() : "");
                                String modText2 = enabled.get(index + 1).getName() +  (!mcFont.isEnabled() ? "&7" : "\u00a77") + sep2;

                                if(lowercased.isEnabled())
                                    modText2 = modText2.toLowerCase();

                                if(flipHorizontally.isEnabled())
                                    Gui.drawRect(x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText2) + 5 : font.getStringWidth(modText2) + 4), y + i + font.getHeight() + height + 1, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + i + font.getHeight() + height + 2, getColor(i2));
                                else
                                    Gui.drawRect(x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText2) + 4 : font.getStringWidth(modText2) + 3), y + i + font.getHeight() + height + 1, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y + i + font.getHeight() + height + 2, getColor(i2));
                            } else {
                                if(!flipHorizontally.isEnabled())
                                    Gui.drawRect(x + width + 1, y + i + 9 + height, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + i + 10 + height, getColor(i2));
                                else
                                    Gui.drawRect(x + width - 1, y + i + 9 + height, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 5), y + i + 10 + height, getColor(i2));

                            }
                            break;
                    }
					/*if(flipVertically.isEnabled())
						i -= 9 + height;
					else*/
                    i += 9 + height;

                    i2 += 9;
                }
            }
        }
    }

    public int getColor(long index) {
        return ((HudSettings)Wrapper.getModuleManager().getModule("Hud Settings")).getColor(index);
    }

}
