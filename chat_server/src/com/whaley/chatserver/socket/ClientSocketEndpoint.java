package com.whaley.chatserver.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketEndpoint implements ClientEndpoint {

	private Socket socket;
	
	public ClientSocketEndpoint(Socket socket) {
		if (socket == null) {
			throw new IllegalArgumentException("socket should not be null");
		}
		
		this.socket = socket;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return this.socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.socket.getOutputStream();
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

	@Override
	public String getInetAddress() {
		return this.socket.getInetAddress().toString();
	}

}
