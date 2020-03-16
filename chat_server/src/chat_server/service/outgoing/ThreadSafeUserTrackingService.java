package chat_server.service.outgoing;

import java.io.PrintStream;
import java.util.Map;

public class ThreadSafeUserTrackingService extends UserTrackingService {

	public ThreadSafeUserTrackingService(Map<String, PrintStream> clients, Map<String, Long> tracker, long timeoutIntervalMs) {
		super(clients, tracker, timeoutIntervalMs);
	}

	@Override
	protected void checkTrackerForTimeouts(long currentTime) {
		synchronized (this.getClientTracker()) {
			super.checkTrackerForTimeouts(currentTime);
		}
	}

	@Override
	protected void untrack(String username) {
		synchronized (this.getClientStreams()) {
			super.untrack(username);
		}
	}

}
