package client.rapid.gui.dropgui;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import client.rapid.Wrapper;
import client.rapid.gui.GuiPosition;
import client.rapid.gui.dropgui.component.DropComponent;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.hud.HudSettings;
import net.minecraft.client.gui.*;

public class DropdownGui extends GuiScreen {
	public static ArrayList<DropFrame> frames;

	public static int color = 0xFFCC4646;
	public static int background = 0xFF0F0F13;
	public static int backgroundDark = new Color(0xFF0F0F13).darker().getRGB();

	private GuiPosition position;
	
	public static HudSettings hud = ((HudSettings)Wrapper.getModuleManager().getModule("Hud Settings"));

	public DropdownGui() {
		frames = new ArrayList<>();
		int frameX = 5;
		for(Category category : Category.values()) {
			DropFrame frame = new DropFrame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 5;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(Wrapper.getSettingsManager().getSettingByName("Click Gui", "Background").isEnabled())
			Gui.drawRect(0, 0, width, height, 0xCC200000);

		for(DropFrame frame : frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);

			for(DropComponent comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		
		Gui.drawRect(4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4, isInside(mouseX, mouseY, 4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4) ? color : 0xFF0F0F0F);
		Gui.drawRect(5, height - 25, 15 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 5, 0xFF1F1F1F);
		mc.fontRendererObj.drawString("Draggable Hud", 10, height - 19, -1);
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for(DropFrame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
				frame.setDrag(true);
			}

			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1)
				frame.setOpen(!frame.isOpen());

			if(frame.isOpen() && !frame.getComponents().isEmpty()) {
				for (DropComponent component : frame.getComponents()) {
					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
		if(mouseButton == 0 && isInside(mouseX, mouseY, 4, height - 26, 16 + mc.fontRendererObj.getStringWidth("Draggable Hud"), height - 4)) {
    		if(position == null) {
				position = new GuiPosition();
			}

    		mc.displayGuiScreen(position);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(DropFrame frame : frames) {
			if(frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
				for(DropComponent component : frame.getComponents()) {
					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}

	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(DropFrame frame : frames) {
			frame.setDrag(false);
		}

		for(DropFrame frame : frames) {
			if(frame.isOpen() && !frame.getComponents().isEmpty()) {
				for(DropComponent component : frame.getComponents()) {
					component.mouseReleased(mouseX, mouseY, state);
				}
			}
		}
	}
    
    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

}
