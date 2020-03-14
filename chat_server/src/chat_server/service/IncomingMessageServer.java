package chat_server.service;

import java.net.Socket;
import java.util.Queue;

public class IncomingMessageServer extends Server {

	private Queue<String> buffer;
	
	public IncomingMessageServer(int port, Queue<String> buffer) {
		super(port);
		this.buffer = buffer;
	}

	@Override
	protected void handle(Socket client) {
		this.execute(new MessageReadingService(client, this.buffer));
	}

}
