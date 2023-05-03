package client.rapid.module.modules.visual;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRenderWorld;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

@ModuleInfo(getName = "Name Tags", getCategory = Category.VISUAL)
public class NameTags extends Module {
    private final Setting scale = new Setting("Size", this, 1, 0, 3, false);

    public NameTags() {
        add(scale);
    }
    public void onEvent(Event e) {
        if(e instanceof EventRenderWorld && e.isPre()) {
            EventRenderWorld event = (EventRenderWorld) e;
            mc.theWorld.loadedEntityList.forEach(p -> {
                if(p instanceof EntityPlayer && p != mc.thePlayer) {
                    double tickX = p.lastTickPosX + (p.posX - p.lastTickPosX) * event.getPartialTicks();
                    double tickY = p.lastTickPosY + (p.posY - p.lastTickPosY) * event.getPartialTicks();
                    double tickZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * event.getPartialTicks();

                    RenderManager render = mc.getRenderManager();

                    try {
                        renderLivingLabel(p, p.getDisplayName().getFormattedText(), tickX - render.renderPosX, tickY - render.renderPosY, tickZ - render.renderPosZ, render, render.getFontRenderer());
                    } catch(NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    protected void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, RenderManager renderManager, FontRenderer fontRenderer) {
        double distance = mc.thePlayer.getDistanceToEntity(entityIn);

        str += EnumChatFormatting.GRAY + " (" + Math.round(distance) + "m)";

        FontRenderer fontrenderer = fontRenderer;
        float f = 1.6F;
        float f1 = 0.016666668F * f;

        f1 += scale.getValue() / 100;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, -f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;
        if(str.equals("deadmau5")) {
            i = -10;
        }

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
