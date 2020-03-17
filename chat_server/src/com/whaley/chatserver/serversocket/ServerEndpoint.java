package com.whaley.chatserver.serversocket;

import java.io.IOException;

import com.whaley.chatserver.socket.ClientEndpoint;

public interface ServerEndpoint {

	ClientEndpoint accept() throws IOException;
	
	void close() throws IOException;
	
	boolean isClosed();
	
}
