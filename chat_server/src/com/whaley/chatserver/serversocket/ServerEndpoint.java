package com.whaley.chatserver.serversocket;

import java.io.IOException;

import com.whaley.chatserver.socket.ClientEndpoint;

public interface ServerEndpoint {

	ClientEndpoint acceptClientEndpoint() throws IOException;
	
	void closeServerEndpoint() throws IOException;
	
	boolean isClosed();
	
}
