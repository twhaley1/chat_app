package chat_server.chat;

import java.util.LinkedList;
import java.util.Queue;

import chat_server.data.Message;
import chat_server.serversocket.Connectable;
import chat_server.service.Server;
import chat_server.service.incoming.IncomingMessageServer;
import chat_server.service.outgoing.OutgoingMessageServer;

public class Chat {

	private String title;
	
	private Server incoming;
	private Server outgoing;
	
	public Chat(String title, Connectable incomingServer, Connectable outgoingServer) {
		if (title == null) {
			throw new IllegalArgumentException("title should not be null");
		}
		if (incomingServer == null) {
			throw new IllegalArgumentException("incomingService should not be null");
		}
		if (outgoingServer == null) {
			throw new IllegalArgumentException("outgoingService should not be null");
		}
		
		this.title = title;
		Queue<Message> messageBuffer = new LinkedList<Message>();
		this.incoming = new IncomingMessageServer(incomingServer, messageBuffer);
		this.outgoing = new OutgoingMessageServer(outgoingServer, messageBuffer);
	}
	
	public void begin() {
		System.out.println("Starting Services For " + this.title + ".");
		
		Thread incomingThread = new Thread(this.incoming);
		Thread outgoingThread = new Thread(this.outgoing);
		incomingThread.start();
		outgoingThread.start();
		try {
			incomingThread.join();
			outgoingThread.join();
		} catch (InterruptedException e) {
			this.end();
		}
	}
	
	public void end() {
		this.incoming.close();
		this.outgoing.close();
	}
}
