package client.rapid.gui.mainmenu;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import client.rapid.Client;
import client.rapid.gui.alt.AltManager;
import client.rapid.gui.mainmenu.components.MMButton;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.util.ClientUtil;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import client.rapid.util.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class ClientMainMenu extends GuiScreen {
	private static final String path = "rapid/images/panorama/test/";

	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
		new ResourceLocation(path + "4.jpg"),
		new ResourceLocation(path + "3.jpg"),
		new ResourceLocation(path + "2.jpg"),
		new ResourceLocation(path + "1.jpg"),

		new ResourceLocation(path + "4.jpg"),
		new ResourceLocation(path + "4.jpg")
	};

	private static ResourceLocation backgroundTexture;

	public static int panoramaTimer;
	private final boolean outdated;

	// Fonts
	private final MCFontRenderer font = Fonts.normal;
	private final MCFontRenderer small = Fonts.small;

	// Changelog list
	private final String[] changelog = new String[255];

	public void updateScreen() {
		panoramaTimer++;
	}

	public ClientMainMenu() {
		outdated = !ClientUtil.isLatest();
	}

	@Override
	public void initGui() {
		buttonList.add(new MMButton(0, width / 2 - 70, height / 2 + 1, 140, 17, "Singleplayer"));
		buttonList.add(new MMButton(1, width / 2 - 70, height / 2 + 20, 140, 17, "Multiplayer"));
		buttonList.add(new MMButton(2, width / 2 - 70, height / 2 + 39, 140, 17, "Alt Manager"));
		buttonList.add(new MMButton(3, width / 2 - 70, height / 2 + 58, 140, 17, "Settings"));
		buttonList.add(new MMButton(4, width / 2 - 70, height / 2 + 77, 140, 17, "Quit"));
		Client.getInstance().getDiscordRP().updateRPC("In Menu", "");
		backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background",  new DynamicTexture(256, 256));
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		// Render background
		GlStateManager.pushMatrix();
		renderSkybox(partialTicks);
		GlStateManager.popMatrix();

		// Render background for buttons
		Gui.drawRect(width / 2 - 75.5, height / 2 - 6.5, width / 2 + 75.5, height / 2 + 100.5, 0x4F000000);
		RenderUtil.drawBorder(width / 2 - 75.5, height / 2 - 6.5, width / 2 + 75.5, height / 2 + 100.5, 0.5, 0xFF000000);

		// Render logo
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1);
		mc.getTextureManager().bindTexture(new ResourceLocation("rapid/images/rapidlogo.png"));
		Gui.drawModalRectWithCustomSizedTexture(width / 2 - 70, height / 2 - 134, 0, 0, 140, 140, 140, 140);
		GlStateManager.popMatrix();

		String mojangab = "Copyright Mojang AB. Do not distribute!";

		font.drawString(mojangab, width - mc.fontRendererObj.getStringWidth(mojangab) - 7, height - 14, -1);

		super.drawScreen(mouseX, mouseY, partialTicks);

		mc.fontRendererObj.drawStringWithShadow("Changelog - " + Client.getInstance().getVersion() + EnumChatFormatting.RED + (outdated ? " [Outdated]" : ""), 3, 1, -1);

		try {
			URL changelogFile = ClassLoader.getSystemResource("assets/minecraft/rapid/changelog.txt");

			Scanner scanner = new Scanner(changelogFile.openStream());

			int i = 0;
			while (scanner.hasNextLine()) {
				changelog[i] = scanner.nextLine();

				int color = 0xFFFF3030;

				if (changelog[i].contains("+")) {
					color = 0xFF30C530;

				} else if (changelog[i].contains("/")) {
					color = 0xFFFFC530;

				} else if (!changelog[i].contains("-")) {
					color = 0xFF4040C0;
				}

				small.drawString(changelog[i].replace("+", "").replace("-", "").replace("/", ""), 10, 12 + i * 8, -1);

				if (!changelog[i].isEmpty()) {
					Gui.drawRect(3.5, 11.5 + i * 8, 8.5, 16.5 + i * 8, 0xFF000000);
					Gui.drawRect(4, 12 + i * 8, 8, 16 + i * 8, color);
				}
				i++;
			}
			scanner.close();
		} catch (Exception e) {
			changelog[1] = "Failed to get changelog";
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		switch(button.id) {
			case 0: mc.displayGuiScreen(new GuiSelectWorld(this));
				break;
			case 1: mc.displayGuiScreen(new GuiMultiplayer(this));
				break;
			case 2: mc.displayGuiScreen(new AltManager());
				break;
			case 3: mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
				break;
			case 4: mc.shutdown();
				break;
		}
	}

	private static void drawPanorama(float p_73970_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(90, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		int i = 8;

		for(int j = 0; j < i * i; ++j) {
			GlStateManager.pushMatrix();
			float f = ((float)(j % i) / (float)i - 0.5F) / 64.0F;
			float f1 = ((float)(j / i) / (float)i - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(-((float)panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for(int k = 0; k < 6; ++k) {
				GlStateManager.pushMatrix();
				if(k == 1)
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);

				if(k == 2)
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

				if(k == 3)
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);

				if(k == 4)
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

				if(k == 5)
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);

				Minecraft.getMinecraft().getTextureManager().bindTexture(titlePanoramaPaths[k]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	private static void rotateAndBlurSkybox() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;

		for(int j = 0; j < i; ++j) {
			float f = 1.0F / (float)(j + 1);
			int k = width;
			int l = height;
			float f1 = (float)(j - i / 2) / 256.0F;
			worldrenderer.pos((double)k, (double)l, (double)zLevel).tex((double)(0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos((double)k, 0.0D, (double)zLevel).tex((double)(1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, (double)zLevel).tex((double)(1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, (double)l, (double)zLevel).tex((double)(0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	public static void renderSkybox(float p_73971_3_) {
		Minecraft.getMinecraft().getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		drawPanorama(p_73971_3_);
		rotateAndBlurSkybox();
		rotateAndBlurSkybox();
		rotateAndBlurSkybox();
		rotateAndBlurSkybox();
		rotateAndBlurSkybox();
		rotateAndBlurSkybox();
		rotateAndBlurSkybox();
		Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		float f = width > height?120.0F / (float)width:120.0F / (float)height;
		float f1 = (float)height * f / 256.0F;
		float f2 = (float)width * f / 256.0F;
		int i = width;
		int j = height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double)j, (double)zLevel).tex((double)(0.5F - f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double)i, (double)j, (double)zLevel).tex((double)(0.5F - f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double)i, 0.0D, (double)zLevel).tex((double)(0.5F + f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double)zLevel).tex((double)(0.5F + f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

}
