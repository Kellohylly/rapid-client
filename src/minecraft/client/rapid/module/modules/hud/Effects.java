package client.rapid.module.modules.hud;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.gui.GuiPosition;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

@ModuleInfo(getName = "Effects", getCategory = Category.HUD)
public class Effects extends Draggable {
    MCFontRenderer font = Fonts.normal2;

    public Effects() {
        super(0, 0, 100, 40);
        setX(new ScaledResolution(mc).getScaledWidth() - this.getWidth() - 2);
        setY(new ScaledResolution(mc).getScaledHeight() - this.getHeight() - 2);
    }

    public void drawDummy(int mouseX, int mouseY) {
        font.drawCenteredStringWithShadow("Regeneration &70:00", x + width + 2 - (float) font.getStringWidth("Regeneration 0:00") / 2 - 4, y + 30, 13458603);
        font.drawCenteredStringWithShadow("Swiftness II &70:00", x + width + 2 - (float) font.getStringWidth("Swiftness II 0:00") / 2 - 4, y + 20, 8171462);
        font.drawCenteredStringWithShadow("Absorption &70:00", x + width + 2 - (float) font.getStringWidth("Absorption 0:00") / 2 - 4, y + 10, 2445989);
        super.drawDummy(mouseX, mouseY);
    }

    public void onEvent(Event e) {
        if(e instanceof EventRender && e.isPre()) {
            int i2 = 16;

            ArrayList<PotionEffect> collection = new ArrayList<>(mc.thePlayer.getActivePotionEffects());

            if (!collection.isEmpty() && !(mc.currentScreen instanceof GuiPosition)) {
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
                    if(Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Minecraft Font").isEnabled())
                        Gui.drawCenteredString(mc.fontRendererObj, text, x + width + 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, y + 14 + i2, potion.getLiquidColor());
                    else
                        font.drawCenteredStringWithShadow(text, x + width + 2 - (float) font.getStringWidth(text) / 2 + 5, y + 14 + i2, potion.getLiquidColor());
                    i2 -= 10;
                }
            }
        }
    }
}
