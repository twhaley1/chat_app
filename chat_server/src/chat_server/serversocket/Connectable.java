package chat_server.serversocket;

import java.io.IOException;

import chat_server.socket.Streamable;

public interface Connectable {

	Streamable accept() throws IOException;
	
	void close() throws IOException;
	
	boolean isClosed();
	
}
