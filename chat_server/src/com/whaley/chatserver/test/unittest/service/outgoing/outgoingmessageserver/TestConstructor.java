package com.whaley.chatserver.test.unittest.service.outgoing.outgoingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.outgoing.OutgoingMessageServer;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestConstructor {

	private class TestConnectable implements ServerEndpoint {

		@Override
		public ClientEndpoint acceptClientEndpoint() throws IOException {
			return null;
		}

		@Override
		public void closeServerEndpoint() throws IOException {
		}

		@Override
		public boolean isClosed() {
			return false;
		}
		
	}
	
	@Test
	public void testNotAllowNullBuffer() {
		assertThrows(IllegalArgumentException.class, () -> new OutgoingMessageServer(new TestConnectable(), null));
	}

}
