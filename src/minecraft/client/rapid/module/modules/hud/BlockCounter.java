package client.rapid.module.modules.hud;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.gui.GuiPosition;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.player.Scaffold;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

import java.awt.*;

@ModuleInfo(getName = "Block Counter", getCategory = Category.HUD)
public class BlockCounter extends Draggable {
    private final Setting scaffoldOnOnly = new Setting("When scaffold on", this, true);
    private final Setting countAll = new Setting("Count all", this, false);
    private final Setting background = new Setting("Background", this, true);

    private final MCFontRenderer font = Fonts.normal;

    public BlockCounter() {
        super(0, 0, 80, 20);
        setX(new ScaledResolution(mc).getScaledWidth() / 2 - this.getWidth() / 2);
        setY(new ScaledResolution(mc).getScaledHeight() / 2 + this.getHeight());
        add(scaffoldOnOnly, countAll, background);
    }

    @Override
    public void drawDummy(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        mc.fontRendererObj.drawString("69 Blocks", x + (float) width / 2 - (float) mc.fontRendererObj.getStringWidth("69 Blocks") / 2, y + (float) height / 2 - (float) mc.fontRendererObj.FONT_HEIGHT / 2, -1);
        super.drawDummy(mouseX, mouseY);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender) {
            if(scaffoldOnOnly.isEnabled() && !isEnabled("Scaffold"))
                return;

            int blockCount = 0;

            if(countAll.isEnabled()) {
                for (int i = 0; i < 45; ++i) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                        continue;

                    ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    Item item = stack.getItem();

                    if (!(item instanceof ItemBlock) || Scaffold.invalid.contains(((ItemBlock) item).getBlock()))
                        continue;

                    blockCount += stack.stackSize;
                }
            } else {
                ItemStack held = mc.thePlayer.getHeldItem();

                if(held != null && held.stackSize != 0 && held.getItem() instanceof ItemBlock && !Scaffold.invalid.contains(((ItemBlock) held.getItem()).getBlock()))
                    blockCount += held.stackSize;
            }

            if (!(mc.currentScreen instanceof GuiPosition)) {
                HudSettings hud = (HudSettings) Wrapper.getModuleManager().getModule("Hud Settings");

                if (background.isEnabled()) {
                    Gui.drawRect(x - 1, y - 1, x + width + 1, y + height + 1, hud.getColor(0));
                    Gui.drawRect(x, y, x + width, y + height, new Color(0xFF0F0F13).brighter().getRGB());
                }

                if (getBoolean("Hud Settings", "Minecraft Font")) {
                    mc.fontRendererObj.drawString(blockCount + " Blocks", x + (float) width / 2 - (float) mc.fontRendererObj.getStringWidth(blockCount + " Blocks") / 2, y + (float) height / 2 - (float) mc.fontRendererObj.FONT_HEIGHT / 2, -1);
                } else {
                    font.drawString(blockCount + " Blocks", x + (float) width / 2 - (float) font.getStringWidth(blockCount + " Blocks") / 2, y + (float) height / 2 - (float) mc.fontRendererObj.FONT_HEIGHT / 2, -1);
                }
            }
        }
    }
}
