package net.minecraft.client.gui;

import client.rapid.gui.ClientMainMenu;
import client.rapid.gui.alt.AltManager;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
   MCFontRenderer font = Fonts.normal;

   protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
   protected int width;
   protected int height;
   public int xPosition;
   public int yPosition;
   public String displayString;
   public int id;
   public boolean enabled;
   public boolean visible;
   protected boolean hovered;

   private int animation = 1;

   public GuiButton(int buttonId, int x, int y, String buttonText) {
      this(buttonId, x, y, 200, 20, buttonText);
   }

   public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      this.width = 200;
      this.height = 20;
      this.enabled = true;
      this.visible = true;
      this.id = buttonId;
      this.xPosition = x;
      this.yPosition = y;
      this.width = widthIn;
      this.height = heightIn;
      this.displayString = buttonText;
   }

   protected int getHoverState(boolean mouseOver) {
      int i = 1;
      if(!this.enabled) {
         i = 0;
      } else if(mouseOver) {
         i = 2;
      }

      return i;
   }

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
         
         if(mc.currentScreen instanceof ClientMainMenu || mc.currentScreen instanceof AltManager) {
        	 Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x30000000);
        	 Gui.drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, 0x40000000);

            if(hovered) {
               if(animation < (mc.currentScreen instanceof AltManager ? (width / 2 - 1) : (width / 2)))
                  animation += 8;

               Gui.drawRect(xPosition + width / 2, yPosition + height + 1, xPosition + width / 2 + animation - 4, yPosition + height, 0xFFFF2020);
               Gui.drawRect(xPosition + width / 2, yPosition + height + 1, xPosition + width / 2 - animation + 4, yPosition + height, 0xFFFF2020);
            } else {
               if(animation > 1) {
                  Gui.drawRect(xPosition + width / 2, yPosition + height + 1, (xPosition + width / 2) + animation - 4, yPosition + height, 0xFFFF2020);
                  Gui.drawRect(xPosition + width / 2, yPosition + height + 1, (xPosition + width / 2) - animation + 4, yPosition + height, 0xFFFF2020);
                  animation -= 8;
               } else
                  animation = 1;

            }

         } else {
        	this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
         	this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
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

   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
   }

   public void mouseReleased(int mouseX, int mouseY) {
   }

   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
   }

   public boolean isMouseOver() {
      return this.hovered;
   }

   public void drawButtonForegroundLayer(int mouseX, int mouseY) {
   }

   public void playPressSound(SoundHandler soundHandlerIn) {
      soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
   }

   public int getButtonWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }
}
