package com.whaley.chatserver.serversocket;

import java.io.IOException;
import java.net.ServerSocket;

import com.whaley.chatserver.socket.ClientEndpoint;
import com.whaley.chatserver.socket.ClientSocketEndpoint;

public class ServerSocketEndpoint implements ServerEndpoint {

	private ServerSocket socket;
	
	public ServerSocketEndpoint(int port) throws IOException {
		this.socket = new ServerSocket(port);
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
