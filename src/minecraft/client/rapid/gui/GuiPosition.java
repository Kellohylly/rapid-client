package client.rapid.gui;

import client.rapid.Wrapper;
import client.rapid.module.Draggable;
import net.minecraft.client.gui.*;

public class GuiPosition extends GuiScreen {

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Draggable d : Wrapper.getModuleManager().getDraggables()) {
			d.drawDummy(mouseX, mouseY);

			Gui.drawRect(d.getX(), d.getY(), d.getX() + d.getWidth(), d.getY() - 0.5, 0xFF9F9F9F);
			Gui.drawRect(d.getX(), d.getY(), d.getX() - 0.5, d.getY() + d.getHeight(), 0xFF9F9F9F);
			Gui.drawRect(d.getX(), d.getY() + d.getHeight(), d.getX() + d.getWidth(), d.getY() + d.getHeight() + 0.5, 0xFF9F9F9F);
			Gui.drawRect(d.getX() + d.getWidth(), d.getY(), d.getX() + d.getWidth() + 0.5, d.getY() + d.getHeight(), 0xFF9F9F9F);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
