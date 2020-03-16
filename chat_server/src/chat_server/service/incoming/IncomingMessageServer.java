package chat_server.service.incoming;

import java.io.IOException;
import java.util.Queue;

import chat_server.data.Message;
import chat_server.serversocket.Connectable;
import chat_server.service.Server;
import chat_server.socket.Streamable;

public class IncomingMessageServer extends Server {

	private Queue<Message> buffer;
	
	public IncomingMessageServer(Connectable endpoint, Queue<Message> buffer) {
		super(endpoint, Runtime.getRuntime().availableProcessors());
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.buffer = buffer;
	}

	@Override
	protected void handle(Streamable client) throws IOException {
		this.execute(new WaitingMessageReadingService(client, this.buffer));
	}

}
