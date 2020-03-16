package chat_server.test.service.incoming.messagereadingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import chat_server.socket.Streamable;
import chat_server.data.Message;
import chat_server.service.incoming.MessageReadingService;

public class TestRun {

	private class TestStreamable implements Streamable {
		
		private boolean isClosed;
		
		private InputStream is;
		private OutputStream os;
		
		public TestStreamable(String input) {
			this.is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
			this.os = new ByteArrayOutputStream();
			this.isClosed = false;
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
			this.isClosed = true;
		}

		@Override
		public String getInetAddress() {
			return null;
		}
		
		public boolean isClosed() {
			return this.isClosed;
		}
	}
	
	private class FailingStreamable implements Streamable {

		@Override
		public InputStream getInputStream() throws IOException {
			throw new IOException();
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
			return null;
		}
		
	}
	
	@Test
	public void testHandlesFailingStream() {
		Streamable stream = new FailingStreamable();
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testAddsToBuffer() {
		TestStreamable stream = new TestStreamable("twhal:How are you!?:9324890234");
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(1, buffer.size());
	}
	
	@Test
	public void testClosesAfterAddition() {
		TestStreamable stream = new TestStreamable("twhal:How are you!?:9324890234");
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(true, stream.isClosed());
	}
	
	@Test
	public void testClosesAfterFailedAddition() {
		TestStreamable stream = new TestStreamable("");
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(true, stream.isClosed());
	}

	@Test
	public void testEmptyStringDoesNotAddToBuffer() {
		TestStreamable stream = new TestStreamable("");
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testNewlineDoesNotAddToBuffer() {
		TestStreamable stream = new TestStreamable(System.lineSeparator());
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(0, buffer.size());
	}
	
	@Test
	public void testInvalidFormatDoesNotAddToBuffer() {
		TestStreamable stream = new TestStreamable("Hey, How ya doing twhal??");
		Queue<Message> buffer = new LinkedList<Message>();
		MessageReadingService service = new MessageReadingService(stream, buffer);
		service.run();
		
		assertEquals(0, buffer.size());
	}
}
