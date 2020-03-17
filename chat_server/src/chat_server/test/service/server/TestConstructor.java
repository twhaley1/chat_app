package chat_server.test.service.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

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

		protected TestServer(ServerEndpoint serverEndpoint, int numberOfThreads) {
			super(serverEndpoint, numberOfThreads);
		}

		@Override
		protected void handle(ClientEndpoint client) throws IOException {
		}
		
	}
	
	@Test
	public void testNotAllowNullEndpoint() {
		assertThrows(IllegalArgumentException.class, () -> new TestServer(null, 3));
	}
	
	@Test
	public void testNotAllowInvalidNumberOfThreads() {
		assertThrows(IllegalArgumentException.class, () -> new TestServer(new TestServerEndpoint(), 0));
	}

	@Test
	public void testNotAllowNegativeNumberOfThreads() {
		assertThrows(IllegalArgumentException.class, () -> new TestServer(new TestServerEndpoint(), -5));
	}
	
}
