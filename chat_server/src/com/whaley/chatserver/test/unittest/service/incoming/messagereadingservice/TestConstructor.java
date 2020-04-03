package com.whaley.chatserver.test.unittest.service.incoming.messagereadingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.incomingmessages.messagereading.MessageReadingService;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestConstructor {

	private class TestStreamable implements ClientEndpoint {

		@Override
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return null;
		}

		@Override
		public void close() throws IOException {	
		}

		@Override
		public String getInetAddress() {
			return null;
		}
		
	}
	
	@Test
	public void testNotAllowNullStreamable() {
		assertThrows(IllegalArgumentException.class, () -> new MessageReadingService(null, new SynchronizedQueue<Message>()));
	}

	@Test
	public void testNotAllowNullQueue() {
		assertThrows(IllegalArgumentException.class, () -> new MessageReadingService(new TestStreamable(), null));
	}

}
