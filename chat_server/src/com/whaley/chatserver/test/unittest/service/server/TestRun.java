package com.whaley.chatserver.test.unittest.service.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestRun {
	
	private class TestServerEndpoint implements ServerEndpoint {

		private boolean isClosed;
		
		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			throw new IOException();
		}

		@Override
		public void closeServerEndpoint() throws IOException {
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
		protected void handleClient(ClientEndpoint client) throws IOException {
		}
		
	}
	
	@Test
	public void testProperlyHandlesIOException() {
		Server server = new TestServer(new TestServerEndpoint(), 1);
		server.run();
		
		assertEquals(true, server.isClosed());
	}

}
