package com.whaley.chatserver.test.unittest.service.incoming.incomingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.incoming.IncomingMessageServer;
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
		assertThrows(IllegalArgumentException.class, () -> new IncomingMessageServer(new TestConnectable(), null));
	}

}
