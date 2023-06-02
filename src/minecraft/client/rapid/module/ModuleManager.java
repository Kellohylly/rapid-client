package client.rapid.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.*;
import client.rapid.module.modules.hud.*;
import client.rapid.module.modules.movement.*;
import client.rapid.module.modules.other.*;
import client.rapid.module.modules.player.*;
import client.rapid.module.modules.visual.Animations;
import client.rapid.module.modules.visual.Cape;
import client.rapid.module.modules.visual.ClickGuiToggle;
import client.rapid.module.modules.visual.Crosshair;
import client.rapid.module.modules.visual.ESP;
import client.rapid.module.modules.visual.FullBright;
import client.rapid.module.modules.hud.Watermark;
import client.rapid.module.modules.visual.NameProtect;
import client.rapid.module.modules.visual.NameTags;
import client.rapid.module.modules.visual.NoRender;
import client.rapid.module.modules.visual.TimeChanger;
import client.rapid.module.modules.visual.XRay;
import client.rapid.util.font.Fonts;

public class ModuleManager {
	private final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<Draggable> draggables = new CopyOnWriteArrayList<>();
	
	public ModuleManager() {

		// Add Modules
		addAll(modules,
			// COMBAT
			new AntiFireball(),
			new AutoClicker(),
			new KeepSprint(),
			new AutoArmor(),
			new Criticals(),
			new KillAura(),
			new Velocity(),
			new AntiBot(),
			new AutoPot(),
			new AutoHeal(),
			//new TPAura(),
			new Aimbot(),
			new Regen(),
			new WTap(),

			// MOVEMENT
			new CorrectMovement(),
			new NoJumpDelay(),
			new InventoryMove(),
			new TargetStrafe(),
			new CustomSpeed(),
			new FastLadder(),
			new HighJump(),
			new LongJump(),
			new NoSlow(),
			new Spider(),
			new Flight(),
			new Strafe(),
			new Sprint(),
			new Phase(),
			new Speed(),
			new Step(),

			// OTHER
			new RichPresenceToggle(),
			new HackerDetector(),
			new ChestStealer(),
			new LunarSpoofer(),
			new ChatFilter(),
			new PingSpoof(),
			new FastPlace(),
			new Disabler(),
			new GodMode(),
			new FastBow(),
			new Spammer(),
			new Timer(),
			new Panic(),
			new Blink(),

			// PLAYER
			new InventoryManager(),
			new AutoRespawn(),
			new KillInsults(),
			new BedBreaker(),
			new Perspective(),
			new ChestAura(),
			new NoRotate(),
			new AntiFall(),
			new SafeWalk(),
			new FastMine(),
			new Scaffold(),
			new AutoMLG(),
			new SpinBot(),
			new FastEat(),
			new NoFall(),
			new Fences(),
			new NoWeb(),
			new Jesus(),

			// VISUAL
			new TimeChanger(),
			new FullBright(),
			new Animations(),
			new NameProtect(),
			new Crosshair(),
			new NoRender(),
			new NameTags(),
			new ClickGuiToggle(),
			new Cape(),
			new XRay(),
			new ESP()
		);

		// Add Draggables
		addAll(draggables,
			new HudSettings(),
			new TargetHud(),
			new Watermark(),
			new BlockCounter(),
			new Effects(),
			new Notifications(),
			new SessionInfo(),
			new Scoreboard(),
			new EnabledModules(),
			new PlayerPosition()
			);

		// Add Draggables to Modules
		for(Draggable d : draggables)
			addAll(modules, d);

		Watermark.setWatermark(Watermark.text);

		// Sort them for beauty kinda
		modules.sort(Comparator.comparingInt(mod -> Fonts.normal2.getStringWidth(((Module)mod).getName())).reversed());

	}

	// Add all modules to an array
	public void addAll(CopyOnWriteArrayList list, Module... modules) {
		list.addAll(Arrays.asList(modules));
	}

	public CopyOnWriteArrayList<Module> getModules() {
		return modules;
	}
	
	public CopyOnWriteArrayList<Draggable> getDraggables() {
		return draggables;
	}

	// Get all modules that are set to the specified category
	public ArrayList<Module> getModulesInCategory(Category category) {
		ArrayList<Module> mods = new ArrayList<>();
		modules.stream().filter(m -> m.getCategory() == category).forEach(mods::add);
		return mods;
	}

	// Gets a module
	public Module getModule(Class<? extends Module> module) {
		for(Module m : getModules()) {
			if(m.getClass() == module) {
				return m;
			}
		}
		return null;
	}

	// Gets a module by name
	public Module getModuleByName(String name) {
		for(Module m : getModules()) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	// Gets a module without spaces
	public Module getModuleWithoutSpaces(String name) {
		for(Module m : getModules()) {
			if(m.getName2().equalsIgnoreCase(name))
				return m;
		}
		return null;
	}

}
