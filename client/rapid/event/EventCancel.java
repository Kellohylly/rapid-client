package client.rapid.event;

public class EventCancel {
	protected boolean cancelled;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public void cancel() {
		setCancelled(true);
	}
	
}
