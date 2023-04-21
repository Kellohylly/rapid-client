package client.rapid.module.modules.visual.draggables;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.visual.Hud;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

@ModuleInfo(getName = "Effects", getCategory = Category.VISUAL)
public class Effects extends Draggable {
    MCFontRenderer font = Fonts.normal2;

    public Effects() {
        super(300, 100, 100, 40);
    }

    public void onEvent(Event e) {
        if(e instanceof EventRender && e.isPre()) {
            int i2 = 16;
            ArrayList<PotionEffect> collection = new ArrayList<>(mc.thePlayer.getActivePotionEffects());

            if (!collection.isEmpty()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableLighting();

                for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects()) {
                    Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

                    String s1 = I18n.format(potion.getName());

                    if (potioneffect.getAmplifier() == 1)
                        s1 = s1 + " " + I18n.format("enchantment.level.2");
                    else if (potioneffect.getAmplifier() == 2)
                        s1 = s1 + " " + I18n.format("enchantment.level.3");
                    else if (potioneffect.getAmplifier() == 3)
                        s1 = s1 + " " + I18n.format("enchantment.level.4");


                    String text = s1 + EnumChatFormatting.GRAY + " " + Potion.getDurationString(potioneffect);
                    if(Wrapper.getSettingsManager().getSettingByName("HUD", "Minecraft Font").isEnabled())
                        Gui.drawCenteredString(mc.fontRendererObj, text, x - mc.fontRendererObj.getStringWidth(text) / 2 + 16, y + 14 + i2, potion.getLiquidColor());
                    else
                        font.drawCenteredStringWithShadow(text, x + width + 2 - font.getStringWidth(text) / 2 + 16, y + 14 + i2, potion.getLiquidColor());
                    i2 -= 10;
                }
            }
        }
    }
}
