package chat_server.test.service.incoming.messagereadingservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import chat_server.data.Message;
import chat_server.service.incoming.MessageReadingService;
import chat_server.socket.Streamable;

public class TestConstructor {

	private class TestStreamable implements Streamable {

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
			return null;
		}
		
	}
	
	@Test
	public void testNotAllowNullStreamable() {
		assertThrows(IllegalArgumentException.class, () -> new MessageReadingService(null, new LinkedList<Message>()));
	}

	@Test
	public void testNotAllowNullQueue() {
		assertThrows(IllegalArgumentException.class, () -> new MessageReadingService(new TestStreamable(), null));
	}
}
