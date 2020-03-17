package chat_server.test.service.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import chat_server.serversocket.ServerEndpoint;
import chat_server.service.Server;
import chat_server.socket.ClientEndpoint;

public class TestRun {
	
	private class TestServerEndpoint implements ServerEndpoint {

		private boolean isClosed;
		
		@Override
		public ClientEndpoint accept() throws IOException {
			throw new IOException();
		}

		@Override
		public void close() throws IOException {
			this.isClosed = true;
		}

		@Override
		public boolean isClosed() {
			return this.isClosed;
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
	public void testProperlyHandlesIOException() {
		Server server = new TestServer(new TestServerEndpoint(), 1);
		server.run();
		
		assertEquals(true, server.isClosed());
	}

}
