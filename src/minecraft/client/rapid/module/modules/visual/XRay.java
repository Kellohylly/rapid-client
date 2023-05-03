package client.rapid.module.modules.visual;

import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "X Ray", getCategory = Category.VISUAL)
public class XRay extends Module {

	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
	}
}
