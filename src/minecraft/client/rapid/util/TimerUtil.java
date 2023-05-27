package client.rapid.util;

public final class TimerUtil {
    private long time;

    public TimerUtil() {
        reset();
    }

    public void reset() {
        time = System.nanoTime() / 1000000L;
    }
    
    public long time() {
        return System.nanoTime() / 1000000L - time;
    }

    public boolean sleep(final long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }

    public boolean reached(final long time) {
        return time() >= time;
    }

	public boolean reached(double time) {
        return time() >= time;
	}
}
