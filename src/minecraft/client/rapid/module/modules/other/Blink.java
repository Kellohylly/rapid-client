package client.rapid.module.modules.other;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;

@ModuleInfo(getName = "Blink", getCategory = Category.OTHER)
public class Blink extends Module {
    private final Setting delay = new Setting("Delay", this, 15, 2, 40, true);
    private final Setting lag = new Setting("Lag", this, false);
    private final Setting createClone = new Setting("Create Clone", this, false);

    private final ArrayList<Packet<? extends INetHandler>> packets = new ArrayList<>();

    public Blink() {
        add(delay, lag, createClone);
    }

    @Override
    public void updateSettings() {
        delay.setVisible(lag.isEnabled());
    }

    @Override
    public void onEnable() {
        packets.clear();
        clonePlayer();
    }

    @Override
    public void onDisable() {
        packets.forEach(mc.thePlayer.sendQueue.getNetworkManager()::sendPacket);
        packets.clear();
        removeClone();
    }

    @Override
    public void onEvent(Event e) {
        this.setTag(String.valueOf(packets.size()));

        if(e instanceof EventUpdate && e.isPre()) {
            if(canSend()) {
                packets.forEach(mc.thePlayer.sendQueue.getNetworkManager()::sendPacket);
                packets.clear();
                removeClone();
                clonePlayer();
            }
        }

        if(e instanceof EventPacket) {
            EventPacket event = (EventPacket) e;

            if(event.getPacket() instanceof C0BPacketEntityAction
            || event.getPacket() instanceof C03PacketPlayer
            || event.getPacket() instanceof C02PacketUseEntity
            || event.getPacket() instanceof C0APacketAnimation
            || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {

                if(canSend()) {
                    return;
                }

                packets.add(event.getPacket());
                event.cancel();
            }

        }
    }

    private void clonePlayer() {
        if(createClone.isEnabled()) {
            EntityOtherPlayerMP clone = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());

            clone.inventory = mc.thePlayer.inventory;
            clone.inventoryContainer = mc.thePlayer.inventoryContainer;
            clone.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            clone.rotationYawHead = mc.thePlayer.rotationYawHead;

            mc.theWorld.addEntityToWorld(-6942059, clone);
        }
    }

    private void removeClone() {
        if(createClone.isEnabled()) {
            mc.theWorld.removeEntityFromWorld(-6942059);
        }
    }

    private boolean canSend() {
        return lag.isEnabled() && mc.thePlayer.ticksExisted % delay.getValue() == 0 && !packets.isEmpty();
    }

}
