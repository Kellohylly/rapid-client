package client.rapid.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.Aimbot;
import client.rapid.module.modules.combat.AntiBot;
import client.rapid.module.modules.combat.AutoArmor;
import client.rapid.module.modules.combat.AutoClicker;
import client.rapid.module.modules.combat.Criticals;
import client.rapid.module.modules.combat.KeepSprint;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.modules.combat.Regen;
import client.rapid.module.modules.combat.TargetStrafe;
import client.rapid.module.modules.combat.Velocity;
import client.rapid.module.modules.combat.WTap;
import client.rapid.module.modules.hud.*;
import client.rapid.module.modules.movement.*;
import client.rapid.module.modules.other.ChatFilter;
import client.rapid.module.modules.other.Disabler;
import client.rapid.module.modules.other.FastBow;
import client.rapid.module.modules.other.FastPlace;
import client.rapid.module.modules.other.GodMode;
import client.rapid.module.modules.other.HackerDetector;
import client.rapid.module.modules.other.Panic;
import client.rapid.module.modules.other.PingSpoof;
import client.rapid.module.modules.other.RichPresenceToggle;
import client.rapid.module.modules.other.Spammer;
import client.rapid.module.modules.other.Timer;
import client.rapid.module.modules.player.*;
import client.rapid.module.modules.visual.Animations;
import client.rapid.module.modules.visual.Cape;
import client.rapid.module.modules.visual.ClickGuiToggle;
import client.rapid.module.modules.visual.Crosshair;
import client.rapid.module.modules.visual.ESP;
import client.rapid.module.modules.visual.FullBright;
import client.rapid.module.modules.visual.Watermark;
import client.rapid.module.modules.visual.NameProtect;
import client.rapid.module.modules.visual.NameTags;
import client.rapid.module.modules.visual.NoRender;
import client.rapid.module.modules.visual.TimeChanger;
import client.rapid.module.modules.visual.XRay;

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
			new Aimbot(),
			new Regen(),
			new WTap(),

			// MOVEMENT
			new CorrectMovement(),
			new InventoryMove(),
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
			new ChatFilter(),
			new PingSpoof(),
			new FastPlace(),
			new Disabler(),
			new GodMode(),
			new FastBow(),
			new Spammer(),
			new Timer(),
			new Panic(),

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
