package client.rapid.util.visual;

import java.awt.Color;
import java.util.Objects;

import client.rapid.util.MinecraftUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class RenderUtil extends MinecraftUtil {

    public static void drawBorder(double x, double y, double width, double height, double thickness, int color) {
        Gui.drawRect(x, y, x + thickness, height, color);
        Gui.drawRect(width, y, width - thickness, height, color);
        Gui.drawRect(x, y, width, y + thickness, color);
        Gui.drawRect(x, height, width, height - thickness, color);
    }
	
    public static void chestESPBox(TileEntity entity, Color color) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        double
        x = entity.getPos().getX(),
        y = entity.getPos().getY(),
        z = entity.getPos().getZ();
        
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.color(Objects.requireNonNull(color).getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        RenderGlobal.func_181561_a(new AxisAlignedBB(x - (double) 1 / 2 + 0.05 - x + (x - mc.getRenderManager().renderPosX), (y - mc.getRenderManager().renderPosY), z - (double) 1 / 2 + 0.05 - z + (z - mc.getRenderManager().renderPosZ), x + 1 - 0.05 - x + (x - mc.getRenderManager().renderPosX), y + 1 - 0.05 - y + (y - mc.getRenderManager().renderPosY), z + 1 - 0.05 - z + (z - mc.getRenderManager().renderPosZ)));
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void entityESPBox(Entity entity, Color color) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        double
        x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks,
        y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks,
        z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;

        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.timer.renderPartialTicks;
        
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.rotate(-yaw, 0, 1, 0);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.color(Objects.requireNonNull(color).getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        RenderGlobal.func_181561_a( new AxisAlignedBB(x - entity.width / 2 - 0.05 - x + (x - mc.getRenderManager().renderPosX), (y - mc.getRenderManager().renderPosY), z - entity.width / 2 - 0.05 - z + (z - mc.getRenderManager().renderPosZ), x + entity.width / 2 + 0.05 - x + (x - mc.getRenderManager().renderPosX), y + entity.height + 0.1 - y + (y - mc.getRenderManager().renderPosY), z + entity.width / 2 + 0.05 - z + (z - mc.getRenderManager().renderPosZ)));
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.rotate(yaw, 0, 1, 0);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
