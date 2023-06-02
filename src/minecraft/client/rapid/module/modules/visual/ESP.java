package client.rapid.module.modules.visual;

import java.awt.Color;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.game.EventRender3D;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.hud.HudSettings;
import client.rapid.module.settings.Setting;
import client.rapid.util.visual.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

@ModuleInfo(getName = "ESP", getCategory = Category.VISUAL)
public class ESP extends Module {
	private final Setting players = new Setting("Players", this, true);
	private final Setting monsters = new Setting("Monsters", this, true);
	private final Setting animals = new Setting("Animals", this, true);
	private final Setting villagers = new Setting("Villagers", this, true);
	private final Setting invisibles = new Setting("Invisibles", this, true);
	private final Setting chests = new Setting("Chests", this, true);

	public ESP() {
		add(players, monsters, animals, villagers, invisibles, chests);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventRender3D && e.isPre()) {
            HudSettings hud = (HudSettings) Client.getInstance().getModuleManager().getModule(HudSettings.class);

            for (Entity entity : mc.theWorld.loadedEntityList) {
				if(canESP(entity))
	           	 	RenderUtil.entityESPBox(entity, new Color(hud.getColor(0)));
	            
            }
            for (TileEntity entity : mc.theWorld.loadedTileEntityList) {
				if(!(entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest))
                    continue;

				if(chests.isEnabled())
					RenderUtil.chestESPBox(entity, new Color(hud.getColor(0)));
            }
		}
	}

	private boolean canESP(Entity entity) {
		if(entity instanceof EntityPlayer || entity instanceof EntityMob || entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityAmbientCreature || entity.isInvisible()) {
			if(!players.isEnabled() && entity instanceof EntityPlayer)
				return false;

			if(!monsters.isEnabled() && entity instanceof EntityMob)
				return false;

			if(!animals.isEnabled() && (entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature))
				return false;

			if(!villagers.isEnabled() && entity instanceof EntityVillager)
				return false;

			if(!invisibles.isEnabled() && entity.isInvisible())
				return false;

		}
		return entity != mc.thePlayer && entity.isEntityAlive();
	}
}
