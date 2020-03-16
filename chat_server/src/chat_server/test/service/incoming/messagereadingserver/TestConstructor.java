package chat_server.test.service.incoming.messagereadingserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import chat_server.serversocket.Connectable;
import chat_server.service.incoming.IncomingMessageServer;
import chat_server.socket.Streamable;

public class TestConstructor {

	private class TestConnectable implements Connectable {

		@Override
		public Streamable accept() throws IOException {
			return null;
		}

		@Override
		public void close() throws IOException {	
		}

		@Override
		public boolean isClosed() {
			return false;
		}
		
	}
	
	@Test
	public void testNotAllowNullBuffer() {
		assertThrows(IllegalArgumentException.class, () -> new IncomingMessageServer(new TestConnectable(), null));
	}

}
