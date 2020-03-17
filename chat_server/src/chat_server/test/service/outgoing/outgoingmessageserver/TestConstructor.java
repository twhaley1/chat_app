package chat_server.test.service.outgoing.outgoingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import chat_server.serversocket.ServerEndpoint;
import chat_server.service.outgoing.OutgoingMessageServer;
import chat_server.socket.ClientEndpoint;

public class TestConstructor {

	private class TestConnectable implements ServerEndpoint {

		@Override
		public ClientEndpoint accept() throws IOException {
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
		assertThrows(IllegalArgumentException.class, () -> new OutgoingMessageServer(new TestConnectable(), null, Long.MAX_VALUE));
	}

}
