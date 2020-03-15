package chat_server.service.outgoing;

import java.io.PrintStream;
import java.util.Map;

public class UserTrackingService implements Runnable {

	private static final int ONE_MINUTE = 60000;
	private static final int THIRTY_SECONDS = 30000;
	
	private Map<String, PrintStream> clients;
	private Map<String, Long> tracker;
	
	private volatile boolean isRunning;
	
	public UserTrackingService(Map<String, PrintStream> clients, Map<String, Long> tracker) {
		this.clients = clients;
		this.tracker = tracker;
		this.isRunning = false;
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		long startingTime = System.currentTimeMillis();
		while (this.isRunning) {
			long currentTime = System.currentTimeMillis();
			long changeInTime = currentTime - startingTime;
			if (changeInTime >= THIRTY_SECONDS) {
				synchronized (this.tracker) {
					this.tracker.forEach((username, timestamp) -> {
						long timeSinceLastMessage = currentTime - timestamp;
						if (timeSinceLastMessage >= ONE_MINUTE) {
							synchronized (this.clients) {
								this.clients.remove(username);
							}
						}
					});
				}
			}
		}
	}

	public void shutdown() {
		this.isRunning = false;
	}
}
