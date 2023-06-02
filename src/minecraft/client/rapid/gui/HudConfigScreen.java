package client.rapid.gui;

import client.rapid.Client;
import client.rapid.module.Draggable;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class HudConfigScreen extends GuiScreen {

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Draggable draggable : Client.getInstance().getModuleManager().getDraggables()) {
			if(draggable.isEnabled()) {

				// Draw draggables on screen
				draggable.drawDummy(mouseX, mouseY);

				// Draw outline around draggables
				Gui.drawRect(draggable.getX(), draggable.getY(), draggable.getX() + draggable.getWidth(), draggable.getY() - 0.5, 0xFF9F9F9F);
				Gui.drawRect(draggable.getX(), draggable.getY(), draggable.getX() - 0.5, draggable.getY() + draggable.getHeight(), 0xFF9F9F9F);
				Gui.drawRect(draggable.getX(), draggable.getY() + draggable.getHeight(), draggable.getX() + draggable.getWidth(), draggable.getY() + draggable.getHeight() + 0.5, 0xFF9F9F9F);
				Gui.drawRect(draggable.getX() + draggable.getWidth(), draggable.getY(), draggable.getX() + draggable.getWidth() + 0.5, draggable.getY() + draggable.getHeight(), 0xFF9F9F9F);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
