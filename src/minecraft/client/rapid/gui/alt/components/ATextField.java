package client.rapid.gui.alt.components;

import client.rapid.gui.alt.AltManager;
import client.rapid.util.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

public class ATextField extends GuiTextField {
    Animation animation = new Animation(1, 0.8f);

    private final int width;
    private final int height;
    private FontRenderer fontRendererInstance;

    public ATextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
        this.width = par5Width;
        this.height = par6Height;
        this.fontRendererInstance = fontrendererObj;
    }

    @Override
    public void drawTextBox() {
        if(this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                if (Minecraft.getMinecraft().currentScreen instanceof AltManager) {
                    Gui.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, 0x30000000);
                    Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0x40000000);
                    Gui.drawRect(this.xPosition, this.yPosition + height + 1, this.xPosition + this.width, this.yPosition + this.height, -1);

                    if (isFocused()) {
                        animation.interpolate(width / 2);

                        Gui.drawRect(xPosition + width / 2, yPosition + height + 1, xPosition + width / 2 + animation.getValue(), yPosition + height, 0xFFFF2020);
                        Gui.drawRect(xPosition + width / 2, yPosition + height + 1, xPosition + width / 2 - animation.getValue(), yPosition + height, 0xFFFF2020);
                    } else {
                        if (animation.getValueF() > 0) {
                            Gui.drawRect(xPosition + width / 2, yPosition + height + 1, (xPosition + width / 2) + animation.getValue(), yPosition + height, 0xFFFF2020);
                            Gui.drawRect(xPosition + width / 2, yPosition + height + 1, (xPosition + width / 2) - animation.getValue(), yPosition + height, 0xFFFF2020);
                        }
                        animation.interpolate(0);
                    }
                } else {
                    drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
                    drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
                }
            }
            int i = this.isEnabled?this.enabledColor:this.disabledColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing?this.xPosition + 4:this.xPosition;
            int i1 = this.enableBackgroundDrawing?this.yPosition + (this.height - 8) / 2:this.yPosition;
            int j1 = l;
            if(k > s.length()) {
                k = s.length();
            }

            if(s.length() > 0) {
                String s1 = flag?s.substring(0, j):s;
                j1 = this.fontRendererInstance.drawStringWithShadow(s1, (float)l, (float)i1, i);
            }

            boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k1 = j1;
            if(!flag) {
                k1 = j > 0?l + this.width:l;
            } else if(flag2) {
                k1 = j1 - 1;
                --j1;
            }

            if(s.length() > 0 && flag && j < s.length()) {
                j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float)j1, (float)i1, i);
            }

            if(flag1) {
                if(flag2) {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                } else {
                    this.fontRendererInstance.drawStringWithShadow("_", (float)k1, (float)i1, i);
                }
            }

            if(k != j) {
                int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }
        }
    }
}
