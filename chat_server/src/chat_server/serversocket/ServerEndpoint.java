package chat_server.serversocket;

import java.io.IOException;

import chat_server.socket.ClientEndpoint;

public interface ServerEndpoint {

	ClientEndpoint accept() throws IOException;
	
	void close() throws IOException;
	
	boolean isClosed();
	
}
