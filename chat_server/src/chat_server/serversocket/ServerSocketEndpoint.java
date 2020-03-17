package chat_server.serversocket;

import java.io.IOException;
import java.net.ServerSocket;

import chat_server.socket.ClientEndpoint;
import chat_server.socket.ClientSocketEndpoint;

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
