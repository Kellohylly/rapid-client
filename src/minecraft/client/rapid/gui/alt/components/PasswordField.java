
package client.rapid.gui.alt.components;

import client.rapid.util.animation.Animation;

import client.rapid.util.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

public class PasswordField extends GuiTextField
{
    Animation animation = new Animation(1, 0.8f);

    private final Minecraft mc = Minecraft.getMinecraft();

    public PasswordField(FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(69420, fontrendererObj, x, y, par5Width, par6Height);
    }


    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                RenderUtil.drawBorder(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0.5, 0xFF000000);
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0x50000000);

                if (isFocused()) {
                    animation.interpolate(width / 2);

                    Gui.drawRect(xPosition + width / 2, yPosition + height + 0.5, xPosition + width / 2 + animation.getValue(), yPosition + height, 0xFFFF2020);
                    Gui.drawRect(xPosition + width / 2, yPosition + height + 0.5, xPosition + width / 2 - animation.getValue(), yPosition + height, 0xFFFF2020);
                } else {
                    if (animation.getValueF() > 0) {
                        Gui.drawRect(xPosition + width / 2, yPosition + height + 0.5, (xPosition + width / 2) + animation.getValue(), yPosition + height, 0xFFFF2020);
                        Gui.drawRect(xPosition + width / 2, yPosition + height + 0.5, (xPosition + width / 2) - animation.getValue(), yPosition + height, 0xFFFF2020);
                    }
                    animation.interpolate(0);
                }

            }
            int var1 = this.isEnabled?this.enabledColor:this.disabledColor;
            int var2 = this.cursorPosition - this.lineScrollOffset;
            int var3 = this.selectionEnd - this.lineScrollOffset;
            String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean var5 = var2 >= 0 && var2 <= s.length();
            final boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            final int var7 = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
            final int var8 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
            int var9 = var7;
            if (var3 > s.length()) {
                var3 = s.length();
            }
            if (s.length() > 0) {
                if (var5) {
                    s.substring(0, var2);
                }
                var9 = mc.fontRendererObj.drawString(this.text.replaceAll("(?s).", "*"), var7, var8, var1);
            }
            final boolean var10 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int var11 = var9;
            if (!var5) {
                var11 = ((var2 > 0) ? (var7 + this.width) : var7);
            }
            else if (var10) {
                var11 = var9 - 1;
                --var9;
            }
            if (s.length() > 0 && var5 && var2 < s.length()) {
                mc.fontRendererObj.drawString(s.substring(var2), var9, var8, var1);
            }
            if (var6) {
                if (var10) {
                    Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                } else {
                    mc.fontRendererObj.drawString("_", var11, var8, var1);
                }
            }
            if (var3 != var2) {
                final int var12 = var7 + this.fontRendererInstance.getStringWidth(s.substring(0, var3));
                this.drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }
        }
    }

}
