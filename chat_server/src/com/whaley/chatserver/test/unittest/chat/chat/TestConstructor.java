package com.whaley.chatserver.test.unittest.chat.chat;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.chat.Chat;
import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestConstructor {

	private class TestServerEndpoint implements ServerEndpoint {

		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			return null;
		}

		@Override
		public void closeServerEndpoint() throws IOException {
		}

		@Override
		public boolean isClosed() {
			return false;
		}
		
	}
	
	private class TestServer extends Server {

		protected TestServer() {
			super(new TestServerEndpoint(), 1);
		}

		@Override
		protected void handleClient(ClientEndpoint client) throws IOException {
		}
		
	}
	
	@Test
	public void testNotAllowNullTitle() {
		assertThrows(IllegalArgumentException.class, () -> new Chat(null, new TestServer(), new TestServer(), new TestServer()));
	}

	@Test
	public void testNotAllowNullIncomingServer() {
		assertThrows(IllegalArgumentException.class, () -> new Chat("Hello", null, new TestServer(), new TestServer()));
	}
	
	@Test
	public void testNotAllowNullOutgoingServer() {
		assertThrows(IllegalArgumentException.class, () -> new Chat("Hello", new TestServer(), null, new TestServer()));
	}
}
