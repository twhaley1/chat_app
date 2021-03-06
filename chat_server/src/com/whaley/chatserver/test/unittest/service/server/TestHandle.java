package com.whaley.chatserver.test.unittest.service.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestHandle {

	private class TestClientEndpoint implements ClientEndpoint {

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
			return "127.0.0.1";
		}
		
	}
	
	private class TestServerEndpoint implements ServerEndpoint {

		private int count;
		
		public TestServerEndpoint() {
			this.count = 0;
		}
		
		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			this.count++;
			return new TestClientEndpoint();
		}

		@Override
		public void closeServerEndpoint() throws IOException {
			this.count = 1;
		}

		@Override
		public boolean isClosed() {
			return this.count > 0;
		}
		
	}
	
	private class TestServer extends Server {

		protected TestServer(ServerEndpoint serverEndpoint, int numberOfThreads) {
			super(serverEndpoint, numberOfThreads);
		}

		@Override
		protected void handleClient(ClientEndpoint client) throws IOException {
			throw new IOException();
		}
		
	}
	
	@Test
	public void testProperlyHandlesIOException() {
		Server server = new TestServer(new TestServerEndpoint(), 1);
		PrintStream previousError = System.err;
		OutputStream os = new ByteArrayOutputStream();
		System.setErr(new PrintStream(os));
		
		server.run();
		System.setErr(previousError);
		assertEquals("127.0.0.1: Client Endpoint Closed." + System.lineSeparator(), os.toString());
	}

}
