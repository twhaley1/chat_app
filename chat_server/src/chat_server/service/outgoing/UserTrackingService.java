package chat_server.service.outgoing;

import java.io.PrintStream;
import java.util.Map;

public class UserTrackingService implements Runnable {

	public static final int TIMEOUT_ONE_MINUTE = 60000;
	private static final int THIRTY_SECONDS = 30000;
	
	private Map<String, PrintStream> clients;
	private Map<String, Long> tracker;
	private long timeoutIntervalMs;
	
	private volatile boolean isRunning;
	
	public UserTrackingService(Map<String, PrintStream> clients, Map<String, Long> tracker, long timeoutIntervalMs) {
		if (clients == null) {
			throw new IllegalArgumentException("clients should not be null");
		}
		if (tracker == null) {
			throw new IllegalArgumentException("tracker should not be null");
		}
		if (timeoutIntervalMs < 0) {
			throw new IllegalArgumentException("timeout interval should not be negative");
		}
		
		this.clients = clients;
		this.tracker = tracker;
		this.timeoutIntervalMs = timeoutIntervalMs;
		this.isRunning = false;
	}
	
	@Override
	public final void run() {
		this.isRunning = true;
		long startingTime = System.currentTimeMillis();
		while (this.isRunning()) {
			long currentTime = System.currentTimeMillis();
			if (this.isTimeToCheck(currentTime - startingTime)) {
				this.checkTrackerForTimeouts(currentTime);
				startingTime = currentTime;
			}
		}
	}
	
	protected boolean isTimeToCheck(long elapsedTimeSinceStart) {
		return elapsedTimeSinceStart >= THIRTY_SECONDS;
	}
	
	protected void checkTrackerForTimeouts(long currentTime) {
		this.getClientTracker().forEach((username, timestamp) -> {
			long timeSinceLastMessage = currentTime - timestamp;
			if (timeSinceLastMessage >= this.timeoutIntervalMs) {
				this.untrack(username);
			}
		}); 
	}
	
	protected void untrack(String username) {
		this.getClientStreams().remove(username);
	}
	
	protected final Map<String, PrintStream> getClientStreams() {
		return this.clients;
	}
	
	protected final Map<String, Long> getClientTracker() {
		return this.tracker;
	}

	public boolean isRunning() {
		return this.isRunning;
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
}
