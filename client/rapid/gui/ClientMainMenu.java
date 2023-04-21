package client.rapid.gui;

import java.io.IOException;
import java.util.Scanner;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.gui.alt.AltManager;
import client.rapid.module.modules.other.RichPresenceToggle;
import client.rapid.util.ClientUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.*;

public class ClientMainMenu extends GuiScreen {
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 70, height / 2, 140, 20, "Singleplayer"));
		buttonList.add(new GuiButton(1, width / 2 - 70, height / 2 + 21, 140, 20, "Multiplayer"));
		buttonList.add(new GuiButton(2, width / 2 - 70, height / 2 + 42, 140, 20, "Alt Manager"));
		buttonList.add(new GuiButton(3, width / 2 - 70, height / 2 + 63, 140, 20, "Settings"));
		buttonList.add(new GuiButton(4, width / 2 - 70, height / 2 + 84, 140, 20, "Quit"));
		Client.getInstance().getRichPresence().update("In Menu", "");
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		mc.getTextureManager().bindTexture(new ResourceLocation("rapid/images/background.jpg"));
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);

		Gui.drawRect(width / 2 - 76, height / 2 - 8, width / 2 + 76, height / 2 + 110, 0x40000000);
		Gui.drawRect(width / 2 - 74, height / 2 - 6, width / 2 + 74, height / 2 + 108, 0x40000000);
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1);
		mc.getTextureManager().bindTexture(new ResourceLocation("rapid/images/rapidlogo.png"));
		Gui.drawModalRectWithCustomSizedTexture(width / 2 - 70, height / 2 - 134, 0, 0, 140, 140, 140, 140);
		GlStateManager.popMatrix();

		String[] changelog = new String[255];
		try {
			Scanner scanner = new Scanner(ClassLoader.getSystemResource("assets/minecraft/rapid/changelog.txt").openStream());

			int i = 0;
			while(scanner.hasNextLine()) {
				changelog[i] = scanner.nextLine().replace("&", "\u00a7");
				i++;
			}
		} catch (Exception e) {
			changelog[0] = "Failed to get changelog";
		}

		int i = 0;
		for(String str : changelog) {
			mc.fontRendererObj.drawStringWithShadow(str, 2, 12 + i, -1);
			i += 10;
		}
		String mojangab = "Copyright Mojang AB. Do not distribute!";

		mc.fontRendererObj.drawStringWithShadow(Client.getInstance().getVersion() + EnumChatFormatting.RED + (!ClientUtil.isLatest() ? " [Outdated]" : ""), 3, 1, -1);
		mc.fontRendererObj.drawString(mojangab, width - mc.fontRendererObj.getStringWidth(mojangab) - 2, height - 10, -1);
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
}
