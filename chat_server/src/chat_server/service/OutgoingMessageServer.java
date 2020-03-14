package chat_server.service;

import java.net.Socket;
import java.util.Queue;

public class OutgoingMessageServer extends Server {

	private Queue<String> buffer;
	
	public OutgoingMessageServer(int port, Queue<String> buffer) {
		super(port);
		this.buffer = buffer;
	}

	@Override
	protected void handle(Socket client) {
		this.execute(new MessageSendingService(client, this.buffer));
	}

}
