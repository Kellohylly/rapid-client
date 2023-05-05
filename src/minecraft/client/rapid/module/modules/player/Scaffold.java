package client.rapid.module.modules.player;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.*;
import client.rapid.event.events.player.*;
import client.rapid.gui.GuiPosition;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.visual.Hud;
import client.rapid.module.settings.Setting;
import client.rapid.util.*;
import client.rapid.util.TimerUtil;
import client.rapid.util.block.BlockData;
import client.rapid.util.module.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(getName = "Scaffold", getCategory = Category.PLAYER)
public class Scaffold extends Draggable {
	private final Setting mode = new Setting("Mode", this, "Pre", "Post");
	private final Setting rotations = new Setting("Rotations", this, "None", "Normal", "Keep");
	private final Setting safewalk = new Setting("Safewalk", this, "None", "Simple", "Legit");
	private final Setting tower = new Setting("Tower", this, "None", "NCP", "Slow");
	private final Setting sprint = new Setting("Sprint", this, "None", "Normal", "No Packet");
	private final Setting delay = new Setting("Delay", this, 0, 0, 500, true);
	private final Setting boost = new Setting("Speed Boost", this, 0, 0, 1, false);
	private final Setting placeOnEnd = new Setting("Place on end", this, true);
	private final Setting rayCast = new Setting("Ray Cast", this, true);
	private final Setting strict = new Setting("Strict", this, true);
	private final Setting eagle = new Setting("Eagle", this, true);
	private final Setting keepY = new Setting("Keep Y", this, false);
	private final Setting swing = new Setting("Swing", this, false);
	private final Setting autoDisable = new Setting("Auto Disable", this, true);
	private double funnyY;
	public static boolean rotated = false;

	private BlockData blockData;
	private BlockPos blockPos;

	private float yaw, pitch;

	private final List<Block> invalid = Arrays.asList(
			Blocks.air,
			Blocks.water,
			Blocks.lava,
			Blocks.flowing_water,
			Blocks.flowing_lava,
			Blocks.command_block,
			Blocks.chest,
			Blocks.crafting_table,
			Blocks.enchanting_table,
			Blocks.furnace,
			Blocks.noteblock,
			Blocks.torch
	);

	TimerUtil timer = new TimerUtil();

	public Scaffold() {
		super(200, 200, 80, 20);
		add(mode, rotations, safewalk, tower, sprint, delay, boost, placeOnEnd, rayCast, strict, eagle, keepY, swing, autoDisable);
	}

	@Override
	public void onEnable() {
		rotated = false;
		yaw = mc.thePlayer.rotationYaw;
		pitch = mc.thePlayer.rotationPitch;
	}

	@Override
	public void onDisable() {
		funnyY = MathHelper.floor_double(mc.thePlayer.posY);
		sneak(false);
		rotated = false;
	}

	@Override
	public void drawDummy(int mouseX, int mouseY) {
		Gui.drawRect(x, y, x + width, y + height, 0x90000000);
		mc.fontRendererObj.drawString("Block Count", x + width / 2 - mc.fontRendererObj.getStringWidth("Block Count") / 2, y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
		super.drawDummy(mouseX, mouseY);
	}

	@Override
	public void onEvent(Event e) {
		setTag(mode.getMode());

		if(e instanceof EventSafewalk && e.isPre() && safewalk.getMode().equals("Simple") && !placeOnEnd.isEnabled())
			e.cancel();

		if(e instanceof EventPacket && e.isPre() && sprint.getMode().equals("No Packet")) {
			EventPacket event = (EventPacket)e;

			if(event.getPacket() instanceof C0BPacketEntityAction)
				event.cancel();
		}

		if(e instanceof EventRender && e.isPre()) {
			int blockCount = 0;
			for (int i = 0; i < 45; ++i) {
				if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
					continue;

				ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = stack.getItem();
				if (!(stack.getItem() instanceof ItemBlock) || invalid.contains(((ItemBlock) item).getBlock()))
					continue;

				blockCount += stack.stackSize;
			}
			if (!(mc.currentScreen instanceof GuiPosition)) {
				Hud hud = (Hud) Wrapper.getModuleManager().getModule("HUD");
				Gui.drawRect(x - 1, y - 1, x + width + 1, y + height + 1, hud.getColor(0));
				Gui.drawRect(x, y, x + width, y + height, new Color(0xFF0F0F13).brighter().getRGB());
				mc.fontRendererObj.drawString(blockCount + " Blocks", x + width / 2 - mc.fontRendererObj.getStringWidth(blockCount + " Blocks") / 2, y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
			}
		}

		if(e instanceof EventRotation) {
			EventRotation event = (EventRotation)e;

			if(blockData != null) {
				float[] rots = RotationUtil.getScaffoldRotations(blockData.getPosition(), blockData.getFace());

				switch (rotations.getMode()) {
					case "Normal":
						event.setYaw(mc.thePlayer.rotationYaw - 180);
						event.setPitch(rots[1]);
						break;
					case "Keep":
						if(rotated) {
							yaw = mc.thePlayer.rotationYaw - 180;
							pitch = rots[1];
						}
						event.setYaw(yaw);
						event.setPitch(pitch);
						break;
				}
				if (!rotations.getMode().equals("None")) {
					mc.thePlayer.rotationYawHead = event.getYaw();
					mc.thePlayer.rotationPitchHead = event.getPitch();
				}
					if (PlayerUtil.hasBlockEquipped() && timer.sleep((long) delay.getValue()) && mode.getMode().equals("Post")) {
						if (placeOnEnd.isEnabled() && !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.001D, 0.0)).isEmpty())
							return;

						if(rayCast.isEnabled() && !overBlock(blockData.getFace(), blockData.getPosition(), strict.isEnabled()))
							return;

						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockData.getPosition(), blockData.getFace(), dataToVec(blockData))) {
							if (swing.isEnabled())
								mc.thePlayer.swingItem();
							else
								PacketUtil.sendPacket(new C0APacketAnimation());

					}
				}
			}
		}
		if(e instanceof EventMotion) {
			if (e.isPre()) {
				if(eagle.isEnabled())
					sneak(rotated);

				boolean safewalkthing = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() == Blocks.air;
				if(safewalk.getMode().equals("Legit"))
					sneak(safewalkthing);

				rotated = false;

				if(safewalkthing)
					rotated = true;

				if(mc.thePlayer.onGround && boost.getValue() != 0)
					setMoveSpeed(getMoveSpeed() + boost.getValue());

				if (keepY.isEnabled()) {
					if ((!isMoving() && mc.gameSettings.keyBindJump.isKeyDown()) || (mc.thePlayer.isCollidedVertically || mc.thePlayer.onGround))
						funnyY = MathHelper.floor_double(mc.thePlayer.posY);
				} else
					funnyY = MathHelper.floor_double(mc.thePlayer.posY);

				if (sprint.getMode().equals("None") && mc.thePlayer.isSprinting()) {
					mc.thePlayer.setSprinting(false);
					mc.gameSettings.keyBindSprint.pressed = false;
				} else {
					mc.gameSettings.keyBindSprint.pressed = true;
a
				}

				blockPos = new BlockPos(mc.thePlayer.posX, funnyY - 1.0D, mc.thePlayer.posZ);
				blockData = ScaffoldUtil.getBlockData(blockPos, invalid);

				if (blockData != null) {
					if (mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air && PlayerUtil.hasBlockEquipped() && mc.gameSettings.keyBindJump.isKeyDown() && !isMoving()) {
						mc.thePlayer.motionX = 0;
						mc.thePlayer.motionZ = 0;
						switch (tower.getMode()) {
							case "NCP":
							case "Slow":
								if (!mc.thePlayer.isPotionActive(Potion.jump)) {
									mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
									mc.thePlayer.motionY = tower.getMode().equals("Slow") ? 0.37 : 0.42;
								}
								break;
						}
					}
					if (PlayerUtil.hasBlockEquipped() && timer.sleep((long) delay.getValue()) && mode.getMode().equals("Pre")) {
						if (placeOnEnd.isEnabled() && !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.001D, 0.0)).isEmpty())
							return;

						if(rayCast.isEnabled() && !overBlock(blockData.getFace(), blockData.getPosition(), strict.isEnabled()))
							return;

						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockData.getPosition(), blockData.getFace(), dataToVec(blockData))) {
							if (swing.isEnabled())
								mc.thePlayer.swingItem();
							else
								PacketUtil.sendPacket(new C0APacketAnimation());

						}
					}
				}
			}
		}
	}

	private void sneak(boolean state) {
		KeyBinding sneak = mc.gameSettings.keyBindSneak;

		try {
			Field field = sneak.getClass().getDeclaredField("pressed");
			field.setAccessible(true);
			field.setBoolean(sneak, state);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private Vec3 dataToVec(BlockData data) {
		BlockPos pos = data.getPosition();
		EnumFacing face = data.getFace();

		double x = pos.getX() + 0.5,
				y = pos.getY() + 0.5,
				z = pos.getZ() + 0.5;

		x += (double) face.getFrontOffsetX() / 2;
		z += (double) face.getFrontOffsetZ() / 2;
		y += (double) face.getFrontOffsetY() / 2;

		return new Vec3(x, y, z);
	}

	public boolean overBlock(final EnumFacing enumFacing, final BlockPos pos, final boolean strict) {
		final MovingObjectPosition movingObjectPosition = RaycastUtil.getMouseOver(4.5f);

		if (movingObjectPosition == null) return false;

		final Vec3 hitVec = movingObjectPosition.hitVec;
		if (hitVec == null) return false;

		return movingObjectPosition.getBlockPos() != null && movingObjectPosition.getBlockPos().equals(pos) && (!strict || movingObjectPosition.sideHit == enumFacing);
	}

}
