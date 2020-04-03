package com.whaley.chatserver.test.unittest.service.outgoing.outgoingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoingmessages.ListeningClients;
import com.whaley.chatserver.service.outgoingmessages.OutgoingMessageServer;
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
	
	private class TestSingleCommandServerEndpoint implements ServerEndpoint {

		private String input;
		private int count;
		
		public TestSingleCommandServerEndpoint(String input) {
			this.input = input;
			this.count = 0;
		}
		
		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			this.count++;
			return new TestStreamable(this.input);
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
	
	private class TestDualCommandServerEndpoint implements ServerEndpoint {

		private String enter;
		private String exit;
		private int count;
		
		public TestDualCommandServerEndpoint(String enter, String exit) {
			this.enter = enter;
			this.exit = exit;
			this.count = 0;
		}
		
		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			this.count++;
			return this.count == 1 ? new TestStreamable(this.enter) : new TestStreamable(this.exit);
		}

		@Override
		public void closeServerEndpoint() throws IOException {
			this.count = 2;
		}

		@Override
		public boolean isClosed() {
			return this.count > 1;
		}
	}
	
	@Test
	public void testAddsCorrectSyntax() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestSingleCommandServerEndpoint("enter twhal"), buffer, new ListeningClients());
		
		server.run();
		int size = server.getUsernamesInRoom().size();
		boolean containsTwhal = server.getUsernamesInRoom().contains("twhal");
		server.closeServer();
		
		assertAll(() -> assertEquals(1, size),
				() -> assertEquals(true, containsTwhal));
	}
	
	@Test
	public void testAddsCorrectSyntaxCaseInsensitive() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestSingleCommandServerEndpoint("EnTeR twhal"), buffer, new ListeningClients());
		
		server.run();
		int size = server.getUsernamesInRoom().size();
		boolean containsTwhal = server.getUsernamesInRoom().contains("twhal");
		server.closeServer();
		
		assertAll(() -> assertEquals(1, size),
				() -> assertEquals(true, containsTwhal));
	}
	
	@Test
	public void testAddsCorrectSyntaxTrimmingExtraSpace() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestSingleCommandServerEndpoint("     enter    twhal        "), buffer, new ListeningClients());
		
		server.run();
		int size = server.getUsernamesInRoom().size();
		boolean containsTwhal = server.getUsernamesInRoom().contains("twhal");
		server.closeServer();
		
		assertAll(() -> assertEquals(1, size),
				() -> assertEquals(true, containsTwhal));
	}
	
	@Test
	public void testUnknownCommand() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestSingleCommandServerEndpoint("jbob twhal"), buffer, new ListeningClients());
		
		server.run();
		int size = server.getUsernamesInRoom().size();
		server.closeServer();
		
		assertEquals(0, size);
	}
	
	@Test
	public void testLeavesCorrectSyntax() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestDualCommandServerEndpoint("enter twhal", "leave twhal"), buffer, new ListeningClients());
		
		server.run();
		int size = server.getUsernamesInRoom().size();
		boolean containsTwhal = server.getUsernamesInRoom().contains("twhal");
		server.closeServer();
		
		assertAll(() -> assertEquals(0, size),
				() -> assertEquals(false, containsTwhal));
	}
	
	@Test
	public void testLeaveOnUnknownUsername() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		OutgoingMessageServer server = new OutgoingMessageServer(new TestDualCommandServerEndpoint("enter twhal", "leave werf"), buffer, new ListeningClients());
		
		server.run();
		int size = server.getUsernamesInRoom().size();
		boolean containsTwhal = server.getUsernamesInRoom().contains("twhal");
		server.closeServer();
		
		assertAll(() -> assertEquals(1, size),
				() -> assertEquals(true, containsTwhal));
	}
}
