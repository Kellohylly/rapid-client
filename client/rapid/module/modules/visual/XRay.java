package client.rapid.module.modules.visual;

import client.rapid.module.*;
import client.rapid.module.modules.Category;

@ModuleInfo(getName = "X Ray", getCategory = Category.VISUAL)
public class XRay extends Module {
	
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}
	
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
	}
}
