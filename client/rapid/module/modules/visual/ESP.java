package client.rapid.module.modules.visual;

import java.awt.Color;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRenderWorld;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.visual.RenderUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.*;

@ModuleInfo(getName = "ESP", getCategory = Category.VISUAL)
public class ESP extends Module {
	private final Setting
	players = new Setting("Players", this, true),
	monsters = new Setting("Monsters", this, true),
	animals = new Setting("Animals", this, true),
	villagers = new Setting("Villagers", this, true),
	invisibles = new Setting("Invisibles", this, true),
	chests = new Setting("Chests", this, true);

	public ESP() {
		add(players, monsters, animals, villagers, invisibles, chests);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventRenderWorld && e.isPre()) {
            Hud hud = (Hud)Wrapper.getModuleManager().getModule("HUD");

            for (Entity entity : mc.theWorld.loadedEntityList) {
	            if (entity == mc.thePlayer || entity instanceof EntityHanging || (!invisibles.isEnabled() && entity.isInvisible()) || (!players.isEnabled() && entity instanceof EntityPlayer) || (!monsters.isEnabled() && entity instanceof EntityMob) || (!animals.isEnabled() && (entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityBat)) || (!villagers.isEnabled() && entity instanceof EntityVillager) || (entity instanceof EntityItem || entity instanceof EntityXPOrb) || entity instanceof EntityMinecart)
	                continue;
	
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
}
