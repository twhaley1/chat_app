package chat_server.test.service.outgoing.messagesendingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import chat_server.data.Message;
import chat_server.service.outgoing.MessageSendingService;

public class TestRun {

	private class TestMessageSendingService extends MessageSendingService {

		private int count;
		
		public TestMessageSendingService(Map<String, PrintStream> clients, Map<String, Long> tracker,
				Queue<Message> buffer) {
			super(clients, tracker, buffer);
			this.count = 0;
		}

		@Override
		public boolean isRunning() {
			return this.count < 1;
		}

		@Override
		protected void clearBuffer() {
			super.clearBuffer();
			this.count++;
		}
		
	}
	
	private class TestEmptyBufferMessageSendingService extends MessageSendingService {

		private int count;
		
		public TestEmptyBufferMessageSendingService(Map<String, PrintStream> clients, Map<String, Long> tracker,
				Queue<Message> buffer) {
			super(clients, tracker, buffer);
			this.count = 0;
		}
		
		@Override
		public boolean isRunning() {
			return this.count < 1;
		}

		@Override
		public boolean containsMessages() {
			boolean result = super.containsMessages();
			this.count++;
			return result;
		}
		
	}
	
	@Test
	public void testHandlesNullMessage() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		OutputStream output = new ByteArrayOutputStream();
		clients.put("twhal", new PrintStream(output));
		
		Map<String, Long> tracker = new HashMap<String, Long>();
		Queue<Message> buffer = new LinkedList<Message>();
		buffer.add(null);
		
		MessageSendingService service = new TestEmptyBufferMessageSendingService(clients, tracker, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals(0, tracker.size()),
				() -> assertEquals("", output.toString()));
	}
	
	@Test
	public void testHandlesEmptyBuffer() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		OutputStream output = new ByteArrayOutputStream();
		clients.put("twhal", new PrintStream(output));
		
		Map<String, Long> tracker = new HashMap<String, Long>();
		Queue<Message> buffer = new LinkedList<Message>();
		
		MessageSendingService service = new TestEmptyBufferMessageSendingService(clients, tracker, buffer);
		
		service.run();
		
		assertEquals(0, buffer.size());
	}

	@Test
	public void testHandlesSingleItemBuffer() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		OutputStream output = new ByteArrayOutputStream();
		clients.put("twhal", new PrintStream(output));
		
		Map<String, Long> tracker = new HashMap<String, Long>();
		
		Queue<Message> buffer = new LinkedList<Message>();
		buffer.add(new Message("twhal", "Hey, How are you", 1));
		
		MessageSendingService service = new TestMessageSendingService(clients, tracker, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals(1, tracker.get("twhal")),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), output.toString()));
	}
	
	@Test
	public void testHandlesMultiItemBuffer() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		OutputStream output = new ByteArrayOutputStream();
		clients.put("twhal", new PrintStream(output));
		
		Map<String, Long> tracker = new HashMap<String, Long>();
		
		Queue<Message> buffer = new LinkedList<Message>();
		buffer.add(new Message("twhal", "Hey, How are you", 1));
		buffer.add(new Message("twhal", "What's up DOG", 2));
		
		MessageSendingService service = new TestMessageSendingService(clients, tracker, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals(2, tracker.get("twhal")),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator()
					+ "twhal: What's up DOG" + System.lineSeparator(), output.toString()));
	}
	
	@Test
	public void testMultiClientSingleItemBuffer() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		OutputStream twhalOutput = new ByteArrayOutputStream();
		OutputStream jbobOutput = new ByteArrayOutputStream();
		clients.put("twhal", new PrintStream(twhalOutput));
		clients.put("jbob", new PrintStream(jbobOutput));
		
		Map<String, Long> tracker = new HashMap<String, Long>();
		
		Queue<Message> buffer = new LinkedList<Message>();
		buffer.add(new Message("twhal", "Hey, How are you", 1));
		
		MessageSendingService service = new TestMessageSendingService(clients, tracker, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals(1, tracker.get("twhal")),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), twhalOutput.toString()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), jbobOutput.toString()));
	}
	
	@Test
	public void testMultiClientMultiItemBuffer() {
		Map<String, PrintStream> clients = new HashMap<String, PrintStream>();
		OutputStream twhalOutput = new ByteArrayOutputStream();
		OutputStream jbobOutput = new ByteArrayOutputStream();
		clients.put("twhal", new PrintStream(twhalOutput));
		clients.put("jbob", new PrintStream(jbobOutput));
		
		Map<String, Long> tracker = new HashMap<String, Long>();
		
		Queue<Message> buffer = new LinkedList<Message>();
		buffer.add(new Message("twhal", "Hey, How are you", 1));
		buffer.add(new Message("jbob", "What's up twhal", 2));
		
		MessageSendingService service = new TestMessageSendingService(clients, tracker, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals(1, tracker.get("twhal")),
				() -> assertEquals(2, tracker.get("jbob")),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator()
						+ "jbob: What's up twhal" + System.lineSeparator(), twhalOutput.toString()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator()
					+ "jbob: What's up twhal" + System.lineSeparator(), jbobOutput.toString()));
	}
}
