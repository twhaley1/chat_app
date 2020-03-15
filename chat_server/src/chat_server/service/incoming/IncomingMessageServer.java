package chat_server.service.incoming;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

import chat_server.data.Message;
import chat_server.service.Server;

public class IncomingMessageServer extends Server {

	private Queue<Message> buffer;
	
	public IncomingMessageServer(int port, Queue<Message> buffer) {
		super(port);
		this.buffer = buffer;
	}

	@Override
	protected void handle(Socket client) throws IOException {
		this.execute(new MessageReadingService(client, this.buffer));
	}

}
