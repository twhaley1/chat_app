package com.whaley.chatserver.test.unittest.service.outgoing.messagesendingservice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoingmessages.ListeningClients;
import com.whaley.chatserver.service.outgoingmessages.messagesending.MessageSendingService;

public class TestConstructor {

	@Test
	public void testNotAllowNullRoom() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(null, buffer));
	}
	
	@Test
	public void testNotAllowNullBuffer() {
		ListeningClients room = new ListeningClients();
		assertThrows(IllegalArgumentException.class, () -> new MessageSendingService(room, null));
	}
	
	@Test
	public void testValidConstruction() {
		ListeningClients room = new ListeningClients();
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		MessageSendingService service = new MessageSendingService(room, buffer);
		
		assertEquals(false, service.isRunning());
	}
}
