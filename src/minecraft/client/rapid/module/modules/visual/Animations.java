package client.rapid.module.modules.visual;

import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "Animations", getCategory = Category.VISUAL)
public class Animations extends Module {
	private final Setting mode = new Setting("Mode", this, "1.8", "1.7", "Basic", "Slide", "Rotate", "Astolfo", "Spin", "Exhibition", "Flat", "What");
	private final Setting swingSpeed = new Setting("Swing Speed", this, 6, 6, 50, true);
	private final Setting itemSize = new Setting("Item Size", this, 0.2, 0, 0.4, false);
	private final Setting betterSwing = new Setting("Better Swing", this, false);

	public Animations() {
		add(mode, swingSpeed, itemSize, betterSwing);
	}

}
