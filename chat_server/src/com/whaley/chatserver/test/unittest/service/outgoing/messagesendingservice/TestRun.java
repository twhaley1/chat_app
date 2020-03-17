package com.whaley.chatserver.test.unittest.service.outgoing.messagesendingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.outgoing.ListeningRoom;
import com.whaley.chatserver.service.outgoing.MessageSendingService;

public class TestRun {

	private class TestMessageSendingService extends MessageSendingService {

		private int count;
		
		public TestMessageSendingService(ListeningRoom room, SynchronizedQueue<Message> buffer) {
			super(room, buffer);
			this.count = 0;
		}

		@Override
		public boolean isRunning() {
			return this.count < 1;
		}

		@Override
		protected List<Message> dequeueMessages() {
			this.count++;
			return super.dequeueMessages();
		}
		
	}
	
	private class TestEmptyBufferMessageSendingService extends MessageSendingService {

		private int count;
		
		public TestEmptyBufferMessageSendingService(ListeningRoom room, SynchronizedQueue<Message> buffer) {
			super(room, buffer);
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
		ListeningRoom room = new ListeningRoom();
		OutputStream output = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(null);
		
		MessageSendingService service = new TestEmptyBufferMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("", output.toString()));
	}
	
	@Test
	public void testHandlesEmptyBuffer() {
		ListeningRoom room = new ListeningRoom();
		OutputStream output = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		
		MessageSendingService service = new TestEmptyBufferMessageSendingService(room, buffer);
		
		service.run();
		
		assertEquals(0, buffer.size());
	}

	@Test
	public void testHandlesSingleItemBuffer() {
		ListeningRoom room = new ListeningRoom();
		OutputStream output = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(output));
		
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		buffer.enqueue(new Message("twhal", "Hey, How are you", 1));
		
		MessageSendingService service = new TestMessageSendingService(room, buffer);
		
		service.run();
		
		assertAll(() -> assertEquals(0, buffer.size()),
				() -> assertEquals("twhal: Hey, How are you" + System.lineSeparator(), output.toString()));
	}
	
	@Test
	public void testHandlesMultiItemBuffer() {
		ListeningRoom room = new ListeningRoom();
		OutputStream output = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(output));
		
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
		ListeningRoom room = new ListeningRoom();
		OutputStream twhalOutput = new ByteArrayOutputStream();
		OutputStream jbobOutput = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(twhalOutput));
		room.assignListener("jbob", new PrintStream(jbobOutput));
		
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
		ListeningRoom room = new ListeningRoom();
		OutputStream twhalOutput = new ByteArrayOutputStream();
		OutputStream jbobOutput = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(twhalOutput));
		room.assignListener("jbob", new PrintStream(jbobOutput));
		
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
