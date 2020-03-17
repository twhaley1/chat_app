package com.whaley.chatserver.test.unittest.service.outgoing.messagesendingservice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.outgoing.ListeningRoom;
import com.whaley.chatserver.service.outgoing.MessageSendingService;

public class TestConstructor {

	@Test
	public void testNotAllowNullRoom() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(null, buffer));
	}
	
	@Test
	public void testNotAllowNullBuffer() {
		ListeningRoom room = new ListeningRoom();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(room, null));
	}
	
	@Test
	public void testValidConstruction() {
		ListeningRoom room = new ListeningRoom();
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		MessageSendingService service = new MessageSendingService(room, buffer);
		
		assertEquals(false, service.isRunning());
	}
}
