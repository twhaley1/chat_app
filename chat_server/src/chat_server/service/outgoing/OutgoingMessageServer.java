package chat_server.service.outgoing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

import chat_server.service.Server;

public class OutgoingMessageServer extends Server {

	private Queue<String> buffer;
	private Collection<PrintStream> room;
	private MessageSendingService service;
	
	public OutgoingMessageServer(int port, Queue<String> buffer) {
		super(port);
		this.buffer = buffer;
		this.room = Collections.synchronizedCollection(new ArrayList<PrintStream>());
		this.service = new MessageSendingService(this.room, this.buffer);
		this.execute(this.service);
	}

	@Override
	protected void handle(Socket client) throws IOException {
		new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
		this.room.add(new PrintStream(client.getOutputStream()));
	}

	@Override
	public void close() {
		super.close();
		this.service.shutdown();
		for (PrintStream client : this.room) {
			client.close();
		}
	}
}
