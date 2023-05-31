package client.rapid.module.modules.other;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.settings.Setting;
import client.rapid.notification.Notification;
import client.rapid.notification.NotificationManager;
import client.rapid.notification.NotificationType;
import client.rapid.util.PacketUtil;
import client.rapid.util.TimerUtil;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(getName = "Disabler", getCategory = Category.OTHER)
public class Disabler extends Module {
	//private final Setting mode = new Setting("Mode", this, "Transaction", "Payload", "Vulcan Strafe", "Timer");
	private final Setting time = new Setting("Transaction Delay", this, 300, 100, 500, true);
	private final Setting noPayload = new Setting("No Payload", this, false);
	private final Setting noAbilities = new Setting("No Abilities", this, false);
	private final Setting timer = new Setting("Timer", this, false);
	private final Setting transaction = new Setting("Transaction", this, false);
	private final Setting keepAlive = new Setting("Keep Alive", this, false);
	private final Setting oldVulcanStrafe = new Setting("Old Vulcan Strafe", this, false);
	private final Setting vulcanc08 = new Setting("Vulcan C08", this, false);
	private final Setting omniSprint = new Setting("Omni Sprint", this, false);

	private final List<C0FPacketConfirmTransaction> transactions = new ArrayList<>();
	private final List<C00PacketKeepAlive> keepAlives = new ArrayList<>();

	private final TimerUtil transactionDelay = new TimerUtil();
	private final TimerUtil keepAliveDelay = new TimerUtil();

	public Disabler() {
		add(time, noPayload, noAbilities, timer, transaction, keepAlive, oldVulcanStrafe, vulcanc08, omniSprint);
	}

	@Override
	public void onEnable() {
		if(oldVulcanStrafe.isEnabled())
			NotificationManager.addToQueue(new Notification("Disabler", "This can flag for Auto Block when attacking!", NotificationType.WARNING, 3));

		NotificationManager.addToQueue(new Notification("Note", "Some disablers may require a restart.", NotificationType.INFO, 3));
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if(oldVulcanStrafe.isEnabled() && mc.thePlayer.ticksExisted % 4 == 0 && mc.thePlayer.swingProgress == 0) {
				PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), EnumFacing.DOWN));
			}
			if(transaction.isEnabled() && transactionDelay.sleep((int)time.getValue() * 10L) && transactions.size() > 0)
				PacketUtil.sendPacketSilent(transactions.get(transactions.size() - 1));

			if(keepAlive.isEnabled() && keepAliveDelay.sleep((int)time.getValue() * 10L) && keepAlives.size() > 0)
				PacketUtil.sendPacketSilent(keepAlives.get(keepAlives.size() - 1));

			if(vulcanc08.isEnabled() && mc.thePlayer.ticksExisted % 4 == 0 && KillAura.target == null)
				PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 0, mc.thePlayer.getHeldItem(), 0, 0, 0));

		}
		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket)e;

			if(timer.isEnabled() && event.getPacket() instanceof C03PacketPlayer && mc.thePlayer.ticksExisted % 3 == 0)
				event.cancel();

			if(noPayload.isEnabled() && event.getPacket() instanceof C17PacketCustomPayload)
				event.cancel();

			if(noAbilities.isEnabled() && event.getPacket() instanceof C13PacketPlayerAbilities)
				event.cancel();

			if(transaction.isEnabled() && event.getPacket() instanceof C0FPacketConfirmTransaction) {
				transactions.add(event.getPacket());
				event.cancel();
			}

			if(keepAlive.isEnabled() && event.getPacket() instanceof C00PacketKeepAlive) {
				keepAlives.add(event.getPacket());
				event.cancel();
			}

			if(event.getPacket() instanceof C03PacketPlayer && mc.thePlayer.isSprinting()) {
				PacketUtil.sendPacketSilent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
			}
		}
	}
}
