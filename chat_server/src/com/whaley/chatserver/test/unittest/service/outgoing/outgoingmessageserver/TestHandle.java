package com.whaley.chatserver.test.unittest.service.outgoing.outgoingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.outgoing.OutgoingMessageServer;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestHandle {

	private class TestStreamable implements ClientEndpoint {
		
		private InputStream is;
		private OutputStream os;
		
		public TestStreamable(String input) {
			this.is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
			this.os = new ByteArrayOutputStream();
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			return this.is;
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return this.os;
		}

		@Override
		public void close() throws IOException {
			this.is.close();
			this.os.close();
		}

		@Override
		public String getInetAddress() {
			return "";
		}
		
	}
	
	private class TestOutgoingConnectable implements ServerEndpoint {

		private String input;
		private int count;
		
		public TestOutgoingConnectable(String input) {
			this.input = input;
			this.count = 0;
		}
		
		@Override
		public ClientEndpoint accept() throws IOException {
			this.count++;
			return new TestStreamable(this.input);
		}

		@Override
		public void close() throws IOException {
			this.count = 1;
		}

		@Override
		public boolean isClosed() {
			return this.count > 0;
		}
	}
	
	@Test
	public void testAddsCorrectSyntax() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestOutgoingConnectable("enter twhal"), buffer);
		
		server.run();
		server.close();
		
		assertAll(() -> assertEquals(1, server.getUsernamesInRoom().size()),
				() -> assertEquals(true, server.getUsernamesInRoom().contains("twhal")));
	}

}
