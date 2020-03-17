package com.whaley.chatserver.test.unittest.service.incoming.incomingmessageserver;

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
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.incoming.IncomingMessageServer;
import com.whaley.chatserver.service.incoming.MessageReadingService;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestHandle {

	private class TestClientEndpoint implements ClientEndpoint {

		private InputStream is;
		private OutputStream os;
		
		public TestClientEndpoint(String input) {
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
			return "127.0.0.1";
		}
		
	}
	
	private class TestServerEndpoint implements ServerEndpoint {

		private String clientMessage;
		private int count;
		
		public TestServerEndpoint(String clientMessage) {
			this.clientMessage = clientMessage;
			this.count = 0;
		}
		
		@Override
		public ClientEndpoint accept() throws IOException {
			this.count++;
			return new TestClientEndpoint(this.clientMessage);
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
	
	private class TestIncomingMessageServer extends IncomingMessageServer {

		public TestIncomingMessageServer(ServerEndpoint endpoint, SynchronizedQueue<Message> buffer) {
			super(endpoint, buffer);
		}

		@Override
		protected Runnable createReadingService(ClientEndpoint client) {
			return new MessageReadingService(client, this.getBuffer());
		}
	}
	
	@Test
	public void testTrimsInputBeforeAddingToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("     twhal         :     Hey, how are you?    :  343929340   "), buffer);
		server.run();
		server.close();
		Message addedMessage = buffer.dequeue();
		assertAll(() -> assertEquals("twhal", addedMessage.getUsername()),
				() -> assertEquals("Hey, how are you?", addedMessage.getContent()),
				() -> assertEquals(343929340, addedMessage.getTimestamp()));
	}
	
	@Test
	public void testAddsCorrectMessageToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("twhal:Hey, how are you?:343929340"), buffer);
		server.run();
		server.close();
		Message addedMessage = buffer.dequeue();
		assertAll(() -> assertEquals("twhal", addedMessage.getUsername()),
				() -> assertEquals("Hey, how are you?", addedMessage.getContent()),
				() -> assertEquals(343929340, addedMessage.getTimestamp()));
	}
	
	@Test
	public void testDoesNotAddEmptyStringToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint(""), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddIncorrectlyFormattedStringToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("twhalheyhowareya"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddWithoutTimestampToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("twhal:Hey, how are you:"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddWithoutTimestampAndContentToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("twhal::"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddWithoutUsernameToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint(":Hey, how are you:2943029029"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddWithoutUsernameAndContentToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint(":Hey, how are you:"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddWithoutUsernameAndTimestampToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("::2943029029"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAddWithoutContentToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("twhal::2943029029"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testDoesNotAllowWithoutAllFieldsToBuffer() {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server server = new TestIncomingMessageServer(new TestServerEndpoint("::"), buffer);
		server.run();
		server.close();
		assertEquals(0, buffer.size());
	}
}
