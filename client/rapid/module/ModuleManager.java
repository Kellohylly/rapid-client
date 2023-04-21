package client.rapid.module;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.*;

import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.*;
import client.rapid.module.modules.movement.*;
import client.rapid.module.modules.other.*;
import client.rapid.module.modules.player.*;
import client.rapid.module.modules.visual.*;
import client.rapid.module.modules.visual.draggables.*;

public class ModuleManager {
	private final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<Draggable> draggables = new CopyOnWriteArrayList<>();
	
	public ModuleManager() {
		addAll(modules,
			// COMBAT
			new TargetStrafe(),
			new AutoClicker(),
			new KeepSprint(),
			new AutoArmor(),
			new Criticals(),
			new KillAura(),
			new Velocity(),
			new AntiBot(),
			new Regen(),
			new WTap(),

			// MOVEMENT
			new InventoryMove(),
			new CustomSpeed(),
			new FastLadder(),
			new BoatJump(),
			new LongJump(),
			new NoSlow(),
			new Spider(),
			new Flight(),
			new Sprint(),
			new Speed(),
			new Step(),

			// OTHER
			new RichPresenceToggle(),
			new ChestStealer(),
			new ChatFilter(),
			new PingSpoof(),
			new FastPlace(),
			new Disabler(),
			new GodMode(),
			new FastBow(),
			new Spammer(),
			new Panic(),

			// PLAYER
			new InventoryManager(),
			new AutoRespawn(),
			new KillInsults(),
			new BedBreaker(),
			new NoRotate(),
			new AntiVoid(),
			new SafeWalk(),
			new FastMine(),
			new FastEat(),
			new NoFall(),
			new NoWeb(),
			new Jesus(),

			// VISUAL
			new TimeChanger(),
			new FullBright(),
			new Animations(),
			new NameProtect(),
			new NoRender(),
			new NameTags(),
			new ClickGuiToggle(),
			new XRay(),
			new ESP()
		);
		
		addAll(draggables,
			new TargetHud(),
			new Hud(),
			new Scaffold(),
			new Effects()
		);
		
		for(Draggable d : draggables)
			addAll(modules, d);
		
	}

	public void addAll(CopyOnWriteArrayList list, Module... modules) {
		list.addAll(Arrays.asList(modules));
	}
	
	public CopyOnWriteArrayList<Module> getModules() {
		return modules;
	}
	
	public CopyOnWriteArrayList<Draggable> getDraggables() {
		return draggables;
	}
	
	public ArrayList<Module> getModulesInCategory(Category category) {
		ArrayList<Module> mods = new ArrayList<>();
		modules.stream().filter(m -> m.getCategory() == category).forEach(mods::add);
		return mods;
	}

	public Module getModule(String name) {
		for(Module m : getModules()) {
			if(m.getName().equalsIgnoreCase(name))
				return m;
		}
		return null;
	}

	public Module getModule2(String name) {
		for(Module m : getModules()) {
			if(m.getName2().equalsIgnoreCase(name))
				return m;
		}
		return null;
	}

}
