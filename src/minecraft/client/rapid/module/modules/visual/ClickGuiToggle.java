package client.rapid.module.modules.visual;

import client.rapid.gui.dropgui.DropdownGui;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.GuiScreen;

@ModuleInfo(getName = "Click Gui", getCategory = Category.VISUAL)
public class ClickGuiToggle extends Module {	
	private final Setting mode = new Setting("Mode", this, "Normal", "Dropdown");
	private final Setting theme = new Setting("Theme", this, "Rapid", "Nord", "Material");
	private final Setting outline = new Setting("Outline", this, true);
	private final Setting categoryIcons = new Setting("Category Icons", this, true);
	private final Setting background = new Setting("Background", this, true);
	
	private GuiScreen clickGui;

	public ClickGuiToggle() {
		add(mode, theme, background, outline, categoryIcons);
		setKey(54);
	}
	
	public void onEnable() {
		if(clickGui == null)
			clickGui = mode.getMode().equals("Dropdown") ? new DropdownGui() : new PanelGui();

		switch(mode.getMode()) {
			case "Normal":
				if(clickGui instanceof DropdownGui)
					clickGui = new PanelGui();
				break;
			case "Dropdown":
				if(clickGui instanceof PanelGui)
					clickGui = new DropdownGui();
				break;
		}
		mc.displayGuiScreen(clickGui);

		switch(theme.getMode()) {
			case "Rapid":
				PanelGui.setBackground(PanelGui.rapidadapta);
				PanelGui.setBackgroundDark(PanelGui.rapidadaptaDark);
				break;
			case "Nord":
				PanelGui.setBackground(PanelGui.nord);
				PanelGui.setBackgroundDark(PanelGui.nordDark);
				break;
			case "Material":
				PanelGui.setBackgroundDark(0xFF1E272C);
				PanelGui.setBackground(0xFF263238);
			break;
		}
		toggle();
	}
}
