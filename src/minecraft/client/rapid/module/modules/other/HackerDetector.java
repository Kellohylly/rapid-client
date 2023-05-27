package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.PlayerUtil;
import client.rapid.util.TimerUtil;
import client.rapid.util.module.MoveUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

/* gotta start making a proper one or something */
@ModuleInfo(getName = "Hacker Detector", getCategory = Category.OTHER)
public class HackerDetector extends Module {
    private final Setting mode = new Setting("Mode", this, "Notifications", "Client Side", "Server Side");
    private final Setting flagPlayer = new Setting("Flag Player", this, false);

    private final TimerUtil cooldown = new TimerUtil();
    private final TimerUtil airTime = new TimerUtil();
    private final TimerUtil groundTime = new TimerUtil();
    private final TimerUtil collideTime = new TimerUtil();
    private final TimerUtil useTime = new TimerUtil();

    private static final ArrayList<EntityPlayer> hackers = new ArrayList<>();

    public HackerDetector() {
        add(mode, flagPlayer);
    }

    @Override
    public void onEnable() {
        hackers.clear();
        cooldown.reset();
        airTime.reset();
        groundTime.reset();
        collideTime.reset();
    }

    @Override
    public void onDisable() {
        hackers.clear();
        cooldown.reset();
        airTime.reset();
        groundTime.reset();
        collideTime.reset();
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate && e.isPre()) {
            for(EntityPlayer entity : mc.theWorld.playerEntities) {

                if(entity == mc.thePlayer && !flagPlayer.isEnabled())
                    return;

                Block blockUnder = mc.theWorld.getBlockState(new BlockPos(entity.posX, entity.posY - 1, entity.posZ)).getBlock();
                Block blockAbove = mc.theWorld.getBlockState(new BlockPos(entity.posX, entity.posY + 2, entity.posZ)).getBlock();

                float speed = (float) Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);

                if(!hackers.contains(entity) && entity.ticksExisted > 20) {
                    if (entity.fallDistance > 0 || entity.onGround)
                        airTime.reset();

                    if (!entity.onGround)
                        groundTime.reset();

                    if (!entity.isCollidedHorizontally || !entity.isSneaking())
                        collideTime.reset();

                    if (!entity.isUsingItem())
                        useTime.reset();

                    // Flight (weird but works sometimes)
                    if (blockUnder instanceof BlockAir && entity.fallDistance == 0 && airTime.reached(500))
                        flag(entity, "Flight");

                    if(entity.isUsingItem() && speed > 0.1 && useTime.reached(500))
                        flag(entity, "No Slow");

                    if((entity.isSprinting() && ((entity.isCollidedHorizontally && collideTime.reached(500)) || (entity.isSneaking() && collideTime.reached(500)) || entity.getFoodStats().getFoodLevel() <= 6))
                            || speed > 0.14 && entity.moveForward < 0 && entity.onGround && groundTime.reached(500))
                        flag(entity, "Sprint");
                }

            }
        }
    }

    private void flag(EntityPlayer player, String check) {
        EnumChatFormatting
        gray = EnumChatFormatting.GRAY,
        red = EnumChatFormatting.RED;

        if (cooldown.sleep(mode.getMode().equals("Server Side") ? 3000 : 1000)) {
            switch(mode.getMode()) {
                case "Notifications":
                    NotificationManager.addToQueue(new Notification("Hacker Alert", player.getName() + " failed " + check + "!", NotificationType.WARNING, 3));
                    hackers.add(player);
                    break;
                case "Client Side":
                    String name = red + player.getName() + gray;
                    check = red + check + gray;

                    PlayerUtil.addChatMessage(name + " failed " + check + "!");
                    break;
                case "Server Side":
                    mc.thePlayer.sendChatMessage("AC >> " + player.getName() + " failed " + check + "!");
                    break;
            }
        }
    }
}
