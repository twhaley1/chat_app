package chat_server.service.incoming;

import java.io.IOException;
import java.util.Queue;

import chat_server.data.Message;
import chat_server.serversocket.ServerEndpoint;
import chat_server.service.Server;
import chat_server.socket.ClientEndpoint;

public class IncomingMessageServer extends Server {

	private Queue<Message> buffer;
	
	public IncomingMessageServer(ServerEndpoint endpoint, Queue<Message> buffer) {
		super(endpoint, Runtime.getRuntime().availableProcessors());
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.buffer = buffer;
	}

	@Override
	protected void handle(ClientEndpoint client) throws IOException {
		this.execute(this.createReadingService(client));
	}

	protected Runnable createReadingService(ClientEndpoint client) {
		return new WaitingMessageReadingService(client, this.buffer);
	}
	
	protected final Queue<Message> getBuffer() {
		return this.buffer;
	}
}
