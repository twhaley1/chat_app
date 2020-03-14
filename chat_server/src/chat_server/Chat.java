package chat_server;

import java.util.LinkedList;
import java.util.Queue;

import chat_server.service.Server;
import chat_server.service.incoming.IncomingMessageServer;
import chat_server.service.outgoing.OutgoingMessageServer;

public class Chat {

	private String title;
	
	private Server incoming;
	private Server outgoing;
	
	private Thread incomingThread;
	private Thread outgoingThread;
	
	public Chat(String title, int incomingPort, int outgoingPort) {
		this.title = title;
		Queue<String> messageBuffer = new LinkedList<String>();
		this.incoming = new IncomingMessageServer(incomingPort, messageBuffer);
		this.outgoing = new OutgoingMessageServer(outgoingPort, messageBuffer);
		this.incomingThread = new Thread(this.incoming);
		this.outgoingThread = new Thread(this.outgoing);
	}
	
	public void begin() {
		System.out.println(this.title);
		this.incomingThread.start();
		this.outgoingThread.start();
		try {
			this.incomingThread.join();
			this.outgoingThread.join();
		} catch (InterruptedException e) {
			this.cleanup();
		}
	}
	
	public void cleanup() {
		this.incoming.close();
		this.outgoing.close();
	}
}
