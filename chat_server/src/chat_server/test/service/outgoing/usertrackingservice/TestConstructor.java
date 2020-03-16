package chat_server.test.service.outgoing.usertrackingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import chat_server.service.outgoing.TimeoutService;

public class TestConstructor {

	@Test
	public void testNotAllowNullClientStream() {
		Map<String, Long> tracker = new HashMap<String, Long>();
		assertThrows(IllegalArgumentException.class, () -> new TimeoutService(null, tracker, 0));
	}

	@Test
	public void testNotAllowNullClientTracker() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		assertThrows(IllegalArgumentException.class, () -> new TimeoutService(clients, null, 0));
	}
	
	@Test
	public void testNotAllowNegativeTimeout() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		Map<String, Long> tracker = new HashMap<String, Long>();
		assertThrows(IllegalArgumentException.class, () -> new TimeoutService(clients, tracker, -1));
	}
	
	@Test
	public void testValidConstruction() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		Map<String, Long> tracker = new HashMap<String, Long>();
		TimeoutService service = new TimeoutService(clients, tracker, 0);
		
		assertEquals(false, service.isRunning());
	}
}
