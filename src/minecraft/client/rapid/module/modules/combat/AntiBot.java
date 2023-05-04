package client.rapid.module.modules.combat;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.RotationUtil;
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
	private final Setting remove = new Setting("Remove", this, false);
	private final Setting excludeTeam = new Setting("Exclude Team", this, false);
	private final Setting clearTime = new Setting("Clear in Seconds", this, 180, 60, 300, true);

	private static final ArrayList<Entity> bots = new ArrayList<>();
	private final TimerUtil timer = new TimerUtil();

	public AntiBot() {
		add(mode, maxDistance, remove, excludeTeam, clearTime);
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
		if(e instanceof EventUpdate && e.isPre()) {
			setTag(mode.getMode());
			
			if(timer.sleep((int)clearTime.getValue() * 100L))
				bots.clear();

			switch(mode.getMode()) {
				case "Basic":
					for (Entity entity : mc.theWorld.loadedEntityList) {
						if (entity instanceof EntityPlayer && entity != mc.thePlayer) {

							double distance = mc.thePlayer.getDistanceToEntity(entity);

							if (entity.ticksExisted < 20 && mc.thePlayer.ticksExisted > 10 && distance > 1 && distance < maxDistance.getValue()) {
								if (!excludeTeam.isEnabled() && ((EntityPlayer) entity).isOnSameTeam(mc.thePlayer))
									return;

								if (remove.isEnabled())
									mc.theWorld.removeEntity(entity);

								bots.add(entity);
							}
						}
					}
					break;
				case "Tab":
					for (Entity entity : mc.theWorld.loadedEntityList) {
						if (!getPlayerList().contains(entity) && entity.isInvisible() && entity instanceof EntityPlayer) {
							if (excludeTeam.isEnabled() && ((EntityPlayer) entity).isOnSameTeam(mc.thePlayer))
								return;

							if (remove.isEnabled())
								mc.theWorld.removeEntity(entity);

							bots.add(entity);
						}
					}
					break;
			}
		}
	}

	public List<EntityPlayer> getPlayerList() {
		List<EntityPlayer> list = new ArrayList<>();

		NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
		List<?> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());

		for (Object o : players) {

			NetworkPlayerInfo info = (NetworkPlayerInfo) o;

			if (info == null)
				continue;

			list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}

	public static ArrayList<Entity> getBots() {
		return bots;
	}
}
