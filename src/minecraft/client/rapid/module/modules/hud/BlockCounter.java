package client.rapid.module.modules.hud;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.gui.GuiPosition;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.player.Scaffold;
import client.rapid.module.modules.visual.Watermark;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.awt.*;

@ModuleInfo(getName = "Block Counter", getCategory = Category.HUD)
public class BlockCounter extends Draggable {
    private final Setting scaffoldOnOnly = new Setting("When scaffold on", this, true);

    public BlockCounter() {
        super(200, 200, 80, 20);
        add(scaffoldOnOnly);
    }

    @Override
    public void drawDummy(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        mc.fontRendererObj.drawString("Block Count", x + width / 2 - mc.fontRendererObj.getStringWidth("Block Count") / 2, y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
        super.drawDummy(mouseX, mouseY);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender) {
            if(scaffoldOnOnly.isEnabled() && !isEnabled("Scaffold"))
                return;

            int blockCount = 0;
            for (int i = 0; i < 45; ++i) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = stack.getItem();
                if (!(stack.getItem() instanceof ItemBlock) || Scaffold.invalid.contains(((ItemBlock) item).getBlock()))
                    continue;

                blockCount += stack.stackSize;
            }
            if (!(mc.currentScreen instanceof GuiPosition)) {
                HudSettings hud = (HudSettings) Wrapper.getModuleManager().getModule("Hud Settings");
                Gui.drawRect(x - 1, y - 1, x + width + 1, y + height + 1, hud.getColor(0));
                Gui.drawRect(x, y, x + width, y + height, new Color(0xFF0F0F13).brighter().getRGB());
                mc.fontRendererObj.drawString(blockCount + " Blocks", x + width / 2 - mc.fontRendererObj.getStringWidth(blockCount + " Blocks") / 2, y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
            }
        }
    }
}
