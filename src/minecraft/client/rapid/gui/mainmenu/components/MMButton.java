package client.rapid.gui.mainmenu.components;

import client.rapid.gui.alt.AltManager;
import client.rapid.gui.mainmenu.ClientMainMenu;
import client.rapid.util.animation.Animation;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class MMButton extends GuiButton {
    Animation animation = new Animation(1, 0.8f);
    MCFontRenderer font = Fonts.normal;

    public MMButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x30000000);
            Gui.drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, 0x40000000);

            if(hovered) {
                animation.interpolate(width / 2);

                Gui.drawRect(xPosition + width / 2, yPosition + height - 1, xPosition + width / 2 + animation.getValue(), yPosition + height, 0xFFFF2020);
                Gui.drawRect(xPosition + width / 2, yPosition + height - 1, xPosition + width / 2 - animation.getValue(), yPosition + height, 0xFFFF2020);
            } else {
                if(animation.getValueF() > 0) {
                    Gui.drawRect(xPosition + width / 2, yPosition + height - 1, (xPosition + width / 2) + animation.getValue(), yPosition + height, 0xFFFF2020);
                    Gui.drawRect(xPosition + width / 2, yPosition + height - 1, (xPosition + width / 2) - animation.getValue(), yPosition + height, 0xFFFF2020);
                }
                animation.interpolate(0);
            }

            this.mouseDragged(mc, mouseX, mouseY);

            int j = 14737632;
            if(!this.enabled) {
                j = 10526880;
            } else if(this.hovered) {
                if(mc.currentScreen instanceof ClientMainMenu || mc.currentScreen instanceof AltManager) {
                    j = 0xFFFF2020;

                }
                else
                    j = 16777120;
            }

            if(mc.currentScreen instanceof ClientMainMenu || mc.currentScreen instanceof AltManager)
                font.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            else
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }
}
