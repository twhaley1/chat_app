package chat_server.test.chat;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import chat_server.chat.Chat;
import chat_server.serversocket.ServerEndpoint;
import chat_server.service.Server;
import chat_server.socket.ClientEndpoint;

public class TestConstructor {

	private class TestServerEndpoint implements ServerEndpoint {

		@Override
		public ClientEndpoint accept() throws IOException {
			return null;
		}

		@Override
		public void close() throws IOException {
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
		protected void handle(ClientEndpoint client) throws IOException {
		}
		
	}
	
	@Test
	public void testNotAllowNullTitle() {
		assertThrows(IllegalArgumentException.class, () -> new Chat(null, new TestServer(), new TestServer()));
	}

	@Test
	public void testNotAllowNullIncomingServer() {
		assertThrows(IllegalArgumentException.class, () -> new Chat("Hello", null, new TestServer()));
	}
	
	@Test
	public void testNotAllowNullOutgoingServer() {
		assertThrows(IllegalArgumentException.class, () -> new Chat("Hello", new TestServer(), null));
	}
}
