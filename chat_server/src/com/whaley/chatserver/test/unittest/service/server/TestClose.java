package com.whaley.chatserver.test.unittest.service.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestClose {
	
	private class TestServerEndpoint implements ServerEndpoint {
		
		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			return null;
		}

		@Override
		public void closeServerEndpoint() throws IOException {
			throw new IOException();
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
		protected void handleClient(ClientEndpoint client) throws IOException {
		}
		
	}
	
	@Test
	public void testProperlyHandlesIOException() {
		Server server = new TestServer(new TestServerEndpoint(), 1);
		PrintStream previousError = System.err;
		OutputStream os = new ByteArrayOutputStream();
		System.setErr(new PrintStream(os));
		
		server.closeServer();
		System.setErr(previousError);
		assertEquals("An IOException Was Thrown When Closing Down A Server." + System.lineSeparator(), os.toString());
	}

}
