package chat_server.test.service.outgoing.outgoingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chat_server.serversocket.ServerEndpoint;
import chat_server.service.outgoing.OutgoingMessageServer;
import chat_server.socket.ClientEndpoint;
import chat_server.data.Message;

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
	
	private OutgoingMessageServer server;
	
	@BeforeEach
	public void setUp() {
		Queue<Message> buffer = new LinkedList<Message>();
		this.server = new OutgoingMessageServer(new TestOutgoingConnectable("twhal"), buffer, Long.MAX_VALUE);
	}
	
	@Test
	public void testProperlyHandlesStreamable() {
		this.server.run();
		this.server.close();
		
		assertAll(() -> assertEquals(1, this.server.getUsernamesInRoom().size()),
				() -> assertEquals(1, this.server.getTrackedUsernames().size()),
				() -> assertEquals(true, this.server.getUsernamesInRoom().contains("twhal")),
				() -> assertEquals(true, this.server.getTrackedUsernames().contains("twhal")));
	}

}
