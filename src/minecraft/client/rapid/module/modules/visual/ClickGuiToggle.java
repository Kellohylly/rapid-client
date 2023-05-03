package client.rapid.module.modules.visual;

import client.rapid.event.events.Event;
import client.rapid.gui.clickgui.ClickGui;
import client.rapid.gui.csgogui.CsgoGui;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.GuiScreen;

@ModuleInfo(getName = "Click Gui", getCategory = Category.VISUAL)
public class ClickGuiToggle extends Module {	
	private final Setting mode = new Setting("Mode", this, "Dropdown", "Normal");
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
			clickGui = mode.getMode().equals("Dropdown") ? new ClickGui() : new CsgoGui();

		switch(mode.getMode()) {
			case "Normal":
				if(clickGui instanceof ClickGui)
					clickGui = new CsgoGui();
				break;
			case "Dropdown":
				if(clickGui instanceof CsgoGui)
					clickGui = new ClickGui();
				break;
		}
		mc.displayGuiScreen(clickGui);

		switch(theme.getMode()) {
			case "Rapid":
				CsgoGui.setBackground(CsgoGui.rapidadapta);
				CsgoGui.setBackgroundDark(CsgoGui.rapidadaptaDark);
				break;
			case "Nord":
				CsgoGui.setBackground(CsgoGui.nord);
				CsgoGui.setBackgroundDark(CsgoGui.nordDark);
				break;
			case "Material":
				CsgoGui.setBackgroundDark(0xFF1E272C);
				CsgoGui.setBackground(0xFF263238);
			break;
		}
		toggle();
	}

	public void onEvent(Event e) {
	}
}
