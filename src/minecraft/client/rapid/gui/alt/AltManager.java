package client.rapid.gui.alt;

import java.io.IOException;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.module.modules.other.RichPresenceToggle;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import client.rapid.gui.ClientMainMenu;
import fr.litarvan.openauth.microsoft.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.*;

public class AltManager extends GuiScreen {
	private GuiTextField username;
	private PasswordField password;
	private String string = "Works for Cracked and Microsoft";

	@Override
	public void initGui() {
        int var3 = height / 4 + 24;
		buttonList.add(new GuiButton(0, width / 2 - 70, height / 2 + 64, 69, 20, "Login"));
		buttonList.add(new GuiButton(2, width / 2 + 1, height / 2 + 64, 69, 20, "Web view"));
		buttonList.add(new GuiButton(1, width / 2 - 70, height / 2 + 86, 140, 20, "Back"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 70, height / 2 + 16, 140, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 70, height / 2 + 40, 140, 20);
        username.setFocused(true);
		Wrapper.getRichPresence().update("In Alt Manager", "");
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.disableAlpha();
		ClientMainMenu.renderSkybox(partialTicks);
		GlStateManager.enableAlpha();

		Gui.drawRect(width / 2 - 76, height / 2 - 8, width / 2 + 76, height / 2 + 110, 0x40000000);
		Gui.drawRect(width / 2 - 74, height / 2 - 6, width / 2 + 74, height / 2 + 108, 0x40000000);
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(3, 3, 3);
		mc.fontRendererObj.drawStringWithShadow("Alt Manager", width / 6 - mc.fontRendererObj.getStringWidth("Alt Manager") / 2, height / 6 - 20, 0xFFFF4646);
		GlStateManager.popMatrix();
		mc.fontRendererObj.drawStringWithShadow(string, width / 2 - mc.fontRendererObj.getStringWidth(string) / 2, height / 2 - 30, -1);

		username.drawTextBox();
		password.drawTextBox();
		
		if(username.getText().isEmpty())
			mc.fontRendererObj.drawString("Username", width / 2 - 66, height / 2 + 22, 0xFF999999);
	
		if(password.getText().isEmpty())
			mc.fontRendererObj.drawString("Password", width / 2 - 66, height / 2 + 46, 0xFF999999);
	
		mc.fontRendererObj.drawString(EnumChatFormatting.GREEN + "Logged into: " + mc.getSession().getUsername(), width / 2 - 70, height / 2, 0xFF999999);

	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		switch(button.id) {
		case 0:
			if(password.getText().isEmpty() && !username.getText().isEmpty()) {
				mc.session = new Session(username.getText(), "", "", "mojang");
	            setText(EnumChatFormatting.GREEN + "Logged into Cracked");
			} else if(!password.getText().isEmpty() && !username.getText().isEmpty()) {
		        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
		        MicrosoftAuthResult result = null;
		        try {
		            result = authenticator.loginWithCredentials(username.getText(), password.getText());
		            mc.session = new Session(result.getProfile().getName(),result.getProfile().getId(), result.getAccessToken(),"legacy");
		            setText(EnumChatFormatting.GREEN + "Logged into Microsoft");
		        } catch (Exception e) {
		            e.printStackTrace();
		            setText(EnumChatFormatting.RED + "Failed to login: " + e.getMessage());
		        }
			}
			break;
		case 1: mc.displayGuiScreen(new ClientMainMenu());
			break;
		case 2: 
	        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
	        MicrosoftAuthResult result = null;
	        try {
		    	result = authenticator.loginWithWebview();
		        mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "legacy");
		        setText(EnumChatFormatting.GREEN + "Logged into Microsoft");
			    } catch (Exception e) {
			        e.printStackTrace();
			        setText(EnumChatFormatting.RED + "Failed to login: " + e.getMessage());
			    }
			break;
		}
	}
	
    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            try {
				this.actionPerformed((GuiButton)this.buttonList.get(0));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }
	
    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(x2, y2, button);
        password.mouseClicked(x2, y2, button);
    }
	
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
	
	@Override
	public void updateScreen() {
		ClientMainMenu.panoramaTimer++;
		super.updateScreen();
        username.updateCursorCounter();
        password.updateCursorCounter();

	}
	
	private void setText(String text) {
		this.string = text;
	}
}
