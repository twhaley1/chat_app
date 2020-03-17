package com.whaley.chatserver.serversocket;

import java.io.IOException;
import java.net.ServerSocket;

import com.whaley.chatserver.socket.ClientEndpoint;
import com.whaley.chatserver.socket.ClientSocketEndpoint;

public class ServerSocketEndpoint implements ServerEndpoint {

	private ServerSocket socket;
	
	public ServerSocketEndpoint(ServerSocket socket) throws IOException {
		if (socket == null) {
			throw new IllegalArgumentException("socket should not be null");
		}
		
		this.socket = socket;
	}

	@Override
	public ClientEndpoint accept() throws IOException {
		return new ClientSocketEndpoint(this.socket.accept());
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

	@Override
	public boolean isClosed() {
		return this.socket.isClosed();
	}

}
