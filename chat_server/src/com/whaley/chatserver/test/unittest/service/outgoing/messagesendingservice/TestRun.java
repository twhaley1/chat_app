package com.whaley.chatserver.test.unittest.service.outgoing.messagesendingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoingmessages.ListeningClients;
import com.whaley.chatserver.service.outgoingmessages.messagesending.MessageSendingService;

public class TestRun {

	private class TestMessageSendingService extends MessageSendingService {

		private int count;
		
		public TestMessageSendingService(ListeningClients room, SynchronizedQueue<Message> buffer) {
			super(room, buffer);
			this.count = 0;
		}

		@Override
		public boolean isRunning() {
			return this.count < 1;
		}

		@Override
		protected List<Message> dequeueMessageBuffer() {
			this.count++;
			return super.dequeueMessageBuffer();
		}
		
	}
	
	private class TestEmptyBufferMessageSendingService extends MessageSendingService {

		private int count;
		
		public TestEmptyBufferMessageSendingService(ListeningClients room, SynchronizedQueue<Message> buffer) {
			super(room, buffer);
			this.count = 0;
		}
		
		@Override
		public boolean isRunning() {
			return this.count < 1;
		}

		@Override
		public boolean bufferContainsMessages() {
			boolean result = super.bufferContainsMessages();
			this.count++;
			return result;
		}
		
	}
	
	@Test
	public void testHandlesNullMessage() {
		ListeningClients room = new ListeningClients();
		OutputStream output = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(null);
		
		MessageSendingService service = new TestEmptyBufferMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("", output.toString()));
	}
	
	@Test
	public void testHandlesEmptyBuffer() {
		ListeningClients room = new ListeningClients();
		OutputStream output = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		
		MessageSendingService service = new TestEmptyBufferMessageSendingService(room, buffer);
		
		service.run();
		
		assertEquals(0, buffer.size());
	}

	@Test
	public void testHandlesSingleItemBuffer() {
		ListeningClients room = new ListeningClients();
		OutputStream output = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(new Message("twhal", "Hey, How are you", 1));
		
		MessageSendingService service = new TestMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), output.toString()));
	}
	
	@Test
	public void testHandlesMultiItemBuffer() {
		ListeningClients room = new ListeningClients();
		OutputStream output = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(new Message("twhal", "Hey, How are you", 1));
		buffer.enqueue(new Message("twhal", "What's up DOG", 2));
		
		MessageSendingService service = new TestMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator()
					+ "twhal: What's up DOG" + System.lineSeparator(), output.toString()));
	}
	
	@Test
	public void testMultiClientSingleItemBuffer() {
		ListeningClients room = new ListeningClients();
		OutputStream twhalOutput = new ByteArrayOutputStream();
		OutputStream jbobOutput = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(twhalOutput));
		room.addClient("jbob", new PrintStream(jbobOutput));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(new Message("twhal", "Hey, How are you", 1));
		
		MessageSendingService service = new TestMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), twhalOutput.toString()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), jbobOutput.toString()));
	}
	
	@Test
	public void testMultiClientMultiItemBuffer() {
		ListeningClients room = new ListeningClients();
		OutputStream twhalOutput = new ByteArrayOutputStream();
		OutputStream jbobOutput = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(twhalOutput));
		room.addClient("jbob", new PrintStream(jbobOutput));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(new Message("twhal", "Hey, How are you", 1));
		buffer.enqueue(new Message("jbob", "What's up twhal", 2));
		
		MessageSendingService service = new TestMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator()
						+ "jbob: What's up twhal" + System.lineSeparator(), twhalOutput.toString()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator()
					+ "jbob: What's up twhal" + System.lineSeparator(), jbobOutput.toString()));
	}
}
