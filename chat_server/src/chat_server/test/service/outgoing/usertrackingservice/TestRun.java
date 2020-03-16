package chat_server.test.service.outgoing.usertrackingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import chat_server.service.outgoing.UserTrackingService;

public class TestRun {

	private class TestUserTrackingService extends UserTrackingService {

		private int count;
		
		public TestUserTrackingService(Map<String, PrintStream> clients, Map<String, Long> tracker, long timeoutIntervalMs) {
			super(clients, tracker, timeoutIntervalMs);
			this.count = 0;
		}

		@Override
		protected boolean isTimeToCheck(long elapsedTimeSinceStart) {
			return true;
		}

		@Override
		public boolean isRunning() {
			boolean result = this.count < 1;
			this.count++;
			return result;
		}
		
	}
	
	private class TestNotTimeToCheckUserTrackingService extends TestUserTrackingService {

		public TestNotTimeToCheckUserTrackingService(Map<String, PrintStream> clients, Map<String, Long> tracker,
				long timeoutIntervalMs) {
			super(clients, tracker, timeoutIntervalMs);
		}
		
		@Override
		protected boolean isTimeToCheck(long elapsedTimeSinceStart) {
			return false;
		}
	}
	
	@Test
	public void testDoesNotRemoveAnythingIfNotTimeToCheck() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		clients.put("twhal", new PrintStream(new ByteArrayOutputStream()));
		Map<String, Long> tracker = new HashMap<String, Long>();
		tracker.put("twhal", 0L);
		
		UserTrackingService service = new TestNotTimeToCheckUserTrackingService(clients, tracker, Long.MAX_VALUE);
		service.run();
		
		assertEquals(1, clients.size());
	}
	
	@Test
	public void testRemovesIfTimedOut() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		clients.put("twhal", new PrintStream(new ByteArrayOutputStream()));
		Map<String, Long> tracker = new HashMap<String, Long>();
		tracker.put("twhal", 0L);
		
		UserTrackingService service = new TestUserTrackingService(clients, tracker, 0);
		service.run();
		
		assertEquals(0, clients.size());
	}

	@Test
	public void testNotRemovedIfNotTimedOut() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		clients.put("twhal", new PrintStream(new ByteArrayOutputStream()));
		Map<String, Long> tracker = new HashMap<String, Long>();
		tracker.put("twhal", 0L);
		
		UserTrackingService service = new TestUserTrackingService(clients, tracker, Long.MAX_VALUE);
		service.run();
		
		assertEquals(1, clients.size());
	}
}
