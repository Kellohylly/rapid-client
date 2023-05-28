package client.rapid.module.modules.visual;

import client.rapid.gui.dropgui.DropdownGui;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

@ModuleInfo(getName = "Click Gui", getCategory = Category.VISUAL)
public class ClickGuiToggle extends Module {	
	private final Setting mode = new Setting("Mode", this, "Normal", "Dropdown");
	private final Setting theme = new Setting("Theme", this, "Rapid", "Nord", "Idk", "SkeetBlue");
	private final Setting outline = new Setting("Outline", this, true);
	private final Setting categoryIcons = new Setting("Category Icons", this, true);
	private final Setting background = new Setting("Background", this, true);
	
	private GuiScreen clickGui;

	public ClickGuiToggle() {
		add(mode, theme, background, outline, categoryIcons);
		setKey(54);
	}

	@Override
	public void settingCheck() {
		categoryIcons.setVisible(mode.getMode().equals("Dropdown"));
	}

	public void onEnable() {
		if(clickGui == null) {
			clickGui = mode.getMode().equals("Dropdown") ? new DropdownGui() : new PanelGui();
		}

		switch(mode.getMode()) {
			case "Normal":
				if(!(clickGui instanceof PanelGui)) {
					clickGui = new PanelGui();
				}
				break;
			case "Dropdown":
				if(!(clickGui instanceof DropdownGui)) {
					clickGui = new DropdownGui();
				}
				break;
		}
		mc.displayGuiScreen(clickGui);

		switch(theme.getMode()) {
			case "Rapid":
				PanelGui.setBackground(PanelGui.rapidadapta);
				PanelGui.setBackgroundDark(PanelGui.rapidadaptaDark);
				break;
			case "Nord":
				PanelGui.setBackgroundDark(new Color(PanelGui.nordDark).darker().darker().getRGB());
				PanelGui.setBackground(new Color(PanelGui.nordDark).darker().getRGB());
				break;
			case "Idk":
				PanelGui.setBackgroundDark(0xFF0d0e13);
				PanelGui.setBackground(0xFF181c28);
				break;
			case "SkeetBlue":
				PanelGui.setBackgroundDark(0xFF0c0c11);
				PanelGui.setBackground(0xFF111119);
				break;
		}
		toggle();
	}
}
