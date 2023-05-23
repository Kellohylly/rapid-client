package client.rapid.module;

import client.rapid.Wrapper;
import org.lwjgl.input.Mouse;

public class Draggable extends Module {
	protected int x, y, lastX, lastY, width, height;
	protected boolean dragging;
	
	public Draggable(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void drawDummy(int mouseX, int mouseY) {
		draw(mouseX, mouseY);

		if(dragging) {
			savePositions();
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void draw(int mouseX, int mouseY) {
		if(dragging) {
			this.x = mouseX + this.lastX;
			this.y = mouseY + this.lastY;

			if(!Mouse.isButtonDown(0))
				this.dragging = false;
		}

		if((mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height)) {
			if(Mouse.isButtonDown(0)) {
				if(!dragging) {
					this.lastX = this.x - mouseX;
					this.lastY = this.y - mouseY;
					this.dragging = true;
				}
			}
		}
		if(dragging) {
			savePositions();
		}
	}

	protected static void savePositions() {
		if (Wrapper.getConfigManager() != null)
			Wrapper.getConfigManager().getHudConfig().save();
	}
}
