package client.rapid.module.modules.combat;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventWorldLoad;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.PlayerUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(getName = "Anti Bot", getCategory = Category.COMBAT)
public class AntiBot extends Module {
	private final Setting mode = new Setting("Mode", this, "Basic", "Tab");
	private final Setting maxDistance = new Setting("Max Distance", this, 6, 2, 10, false);
	private final Setting clear = new Setting("Clear", this, false);
	private final Setting clearTime = new Setting("Clear in Seconds", this, 180, 60, 300, true);
	private final Setting hasArmor = new Setting("Has Armor", this, false);

	private final Setting remove = new Setting("Remove", this, false);
	private final Setting excludeTeam = new Setting("Exclude Team", this, false);

	private static final ArrayList<Entity> bots = new ArrayList<>();
	private final TimerUtil timer = new TimerUtil();

	public AntiBot() {
		add(mode, maxDistance, clear, clearTime, hasArmor, remove, excludeTeam);
	}

	@Override
	public void updateSettings() {
		maxDistance.setVisible(!mode.getMode().equals("Tab"));
		clearTime.setVisible(clear.isEnabled());
		hasArmor.setVisible(mode.getMode().equals("Basic"));
	}

	@Override
	public void onEnable() {
		bots.clear();
	}

	@Override
	public void onDisable() {
		bots.clear();
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventWorldLoad) {
			bots.clear();
		}
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());

			if(clear.isEnabled() && timer.sleep((int)clearTime.getValue() * 100L)) {
				bots.clear();
			}

			switch(mode.getMode()) {
			case "Basic":
				for (EntityPlayer entity : mc.theWorld.playerEntities) {
					if (entity != mc.thePlayer) {

						double distance = mc.thePlayer.getDistanceToEntity(entity);

						// Check players and entities existing time and distance
						if (entity.ticksExisted < 20 && mc.thePlayer.ticksExisted > 10 && distance > 1 && distance < maxDistance.getValue()) {

							// Check if on same team
							if (!excludeTeam.isEnabled() && entity.isOnSameTeam(mc.thePlayer)) {
								return;
							}

							// Check if entity has armor
							if(hasArmor.isEnabled() && !PlayerUtil.hasArmorEquipped(entity)) {
								return;
							}

							// Remove entity from world
							if (remove.isEnabled()) {
								mc.theWorld.removeEntity(entity);
							}

							bots.add(entity);
						}
					}
				}
				break;
			case "Tab":
				for (EntityPlayer entity : mc.theWorld.playerEntities) {

					// Check if entity is not in player list
					if (!getPlayerList().contains(entity)) {

						// Check if on same team
						if (excludeTeam.isEnabled() && entity.isOnSameTeam(mc.thePlayer)) {
							return;
						}

						// Remove entity from world
						if (remove.isEnabled()) {
							mc.theWorld.removeEntity(entity);
						}

						bots.add(entity);
					}
				}
				break;
			}
		}
	}

	// Get list of players
	public List<EntityPlayer> getPlayerList() {
		List<EntityPlayer> list = new ArrayList<>();

		List<?> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.thePlayer.sendQueue.getPlayerInfoMap());

		for (Object player : players) {
			NetworkPlayerInfo info = (NetworkPlayerInfo) player;

			if (info == null) {
				continue;
			}

			EntityPlayer player2 = mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName());

			list.add(player2);
		}
		return list;
	}

	public static ArrayList<Entity> getBots() {
		return bots;
	}

}
