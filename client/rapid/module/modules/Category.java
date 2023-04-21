package client.rapid.module.modules;

public enum Category {
	COMBAT("Combat", 'a'), MOVEMENT("Movement", 'b'), OTHER("Other", 'd'), PLAYER("Player", 'f'), VISUAL("Visual", 'g');
	
	private final String name;
	private final char icon;
	
	Category(String name, char icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}

	public char getIcon() {
		return icon;
	}
}
