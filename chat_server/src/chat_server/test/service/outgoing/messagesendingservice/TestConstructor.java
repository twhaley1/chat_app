package chat_server.test.service.outgoing.messagesendingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import chat_server.data.Message;
import chat_server.service.outgoing.MessageSendingService;

public class TestConstructor {

	@Test
	public void testNotAllowNullClients() {
		Map<String, Long> tracker = new HashMap<String, Long>();
		Queue<Message> buffer = new LinkedList<Message>();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(null, tracker, buffer));
	}

	@Test
	public void testNotAllowNullTracker() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		Queue<Message> buffer = new LinkedList<Message>();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(clients, null, buffer));
	}
	
	@Test
	public void testNotAllowNullBuffer() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		Map<String, Long> tracker = new HashMap<String, Long>();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(clients, tracker, null));
	}
	
	@Test
	public void testValidConstruction() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		Map<String, Long> tracker = new HashMap<String, Long>();
		Queue<Message> buffer = new LinkedList<Message>();
		MessageSendingService service = new MessageSendingService(clients, tracker, buffer);
		
		assertEquals(false, service.isRunning());
	}
}
