package client.rapid.module.modules.player;

import client.rapid.event.Event;
import client.rapid.event.events.game.EventPacket;
import client.rapid.event.events.player.EventCollide;
import client.rapid.event.events.player.EventMotion;
import client.rapid.event.events.player.EventUpdate;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.movement.LongJump;
import client.rapid.module.settings.Setting;
import client.rapid.util.PacketUtil;
import client.rapid.util.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@ModuleInfo(getName = "No Fall", getCategory = Category.PLAYER)
public class NoFall extends Module {
	private final Setting mode = new Setting("Mode", this, "Vanilla", "Ground", "Vulcan", "Verus");
	private final Setting distance = new Setting("Fall Distance", this, 4, 2, 4, false);

	public static int falls;

	public NoFall() {
		add(mode, distance);
	}

	@Override
	public void onEvent(Event e) {
		if(isEnabled(AntiFall.class) && !PlayerUtil.isBlockUnder())
			return;

		if(e instanceof EventCollide && e.isPre() && mode.getMode().equals("Verus")) {
			EventCollide event = (EventCollide)e;

			if(mc.thePlayer.fallDistance >= distance.getValue()) {
				event.setAxisAlignedBB(new AxisAlignedBB(-5, -1, -5, 5, 1, 5).offset(event.getX(), event.getY(), event.getZ()));
			}
		}

		if(e instanceof EventUpdate && e.isPre()) {
			BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

			if(!mc.theWorld.isAirBlock(pos) && mc.theWorld.getBlockState(pos).getBlock().isFullBlock()) {
				falls = 0;
			}

		}
		if(e instanceof EventPacket && e.isPre()) {
			EventPacket event = (EventPacket) e;

			if(mode.getMode().equals("Vulcan") && event.getPacket() instanceof C03PacketPlayer && mc.thePlayer.fallDistance > distance.getValue() && !isEnabled(LongJump.class)) {
				if(falls > 0) {
					mc.thePlayer.motionY = -0.1f;
				} else {
					mc.thePlayer.motionY = -0.07f;
				}

				((C03PacketPlayer) event.getPacket()).setOnGround(true);

				mc.thePlayer.fallDistance = 0;
				falls++;
			}
		}
		if(e instanceof EventMotion && e.isPre()) {
			setTag(mode.getMode());

			if(mc.thePlayer.fallDistance >= distance.getValue()) {
				switch(mode.getMode()) {
				case "Vanilla":
					PacketUtil.sendPacketSilent(new C03PacketPlayer(true));
					break;
				case "Ground":
					((EventMotion)e).setGround(true);
					break;
				}
			}
		}
	}
}
