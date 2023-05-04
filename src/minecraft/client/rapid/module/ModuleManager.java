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
import client.rapid.module.modules.movement.CorrectMovement;
import client.rapid.module.modules.movement.CustomSpeed;
import client.rapid.module.modules.movement.FastLadder;
import client.rapid.module.modules.movement.Flight;
import client.rapid.module.modules.movement.HighJump;
import client.rapid.module.modules.movement.InventoryMove;
import client.rapid.module.modules.movement.Jesus;
import client.rapid.module.modules.movement.LongJump;
import client.rapid.module.modules.movement.NoSlow;
import client.rapid.module.modules.movement.Speed;
import client.rapid.module.modules.movement.Spider;
import client.rapid.module.modules.movement.Sprint;
import client.rapid.module.modules.movement.Step;
import client.rapid.module.modules.movement.Strafe;
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
import client.rapid.module.modules.player.AntiVoid;
import client.rapid.module.modules.player.AutoMLG;
import client.rapid.module.modules.player.AutoRespawn;
import client.rapid.module.modules.player.BedBreaker;
import client.rapid.module.modules.player.ChestAura;
import client.rapid.module.modules.player.ChestStealer;
import client.rapid.module.modules.player.FastEat;
import client.rapid.module.modules.player.FastMine;
import client.rapid.module.modules.player.InventoryManager;
import client.rapid.module.modules.player.KillInsults;
import client.rapid.module.modules.player.NoFall;
import client.rapid.module.modules.player.NoRotate;
import client.rapid.module.modules.player.NoWeb;
import client.rapid.module.modules.player.Perspective;
import client.rapid.module.modules.player.SafeWalk;
import client.rapid.module.modules.player.Scaffold;
import client.rapid.module.modules.player.SpinBot;
import client.rapid.module.modules.visual.Animations;
import client.rapid.module.modules.visual.Cape;
import client.rapid.module.modules.visual.ClickGuiToggle;
import client.rapid.module.modules.visual.Crosshair;
import client.rapid.module.modules.visual.ESP;
import client.rapid.module.modules.visual.FullBright;
import client.rapid.module.modules.visual.Hud;
import client.rapid.module.modules.visual.NameProtect;
import client.rapid.module.modules.visual.NameTags;
import client.rapid.module.modules.visual.NoRender;
import client.rapid.module.modules.visual.Notifications;
import client.rapid.module.modules.visual.Scoreboard;
import client.rapid.module.modules.visual.SessionInfo;
import client.rapid.module.modules.visual.TimeChanger;
import client.rapid.module.modules.visual.XRay;
import client.rapid.module.modules.visual.draggables.Effects;
import client.rapid.module.modules.visual.draggables.TargetHud;

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
			new AntiVoid(),
			new SafeWalk(),
			new FastMine(),
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
			new TargetHud(),
			new Hud(),
			new Scaffold(),
			new Effects(),
			new Notifications(),
			new SessionInfo(),
			new Scoreboard()
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
