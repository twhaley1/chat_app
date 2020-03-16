package chat_server.serversocket;

import java.io.IOException;
import java.net.ServerSocket;

import chat_server.socket.Streamable;
import chat_server.socket.StreamableSocket;

public class ConnectableServerSocket implements Connectable {

	private ServerSocket socket;
	
	public ConnectableServerSocket(int port) throws IOException {
		this.socket = new ServerSocket(port);
	}

	@Override
	public Streamable accept() throws IOException {
		return new StreamableSocket(this.socket.accept());
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
