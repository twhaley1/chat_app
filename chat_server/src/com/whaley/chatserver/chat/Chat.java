package com.whaley.chatserver.chat;

import com.whaley.chatserver.service.Server;

public class Chat {

	private String title;
	
	private Server incoming;
	private Server outgoing;
	
	public Chat(String title, Server incoming, Server outgoing) {
		if (title == null) {
			throw new IllegalArgumentException("title should not be null");
		}
		if (incoming == null) {
			throw new IllegalArgumentException("incoming server should not be null");
		}
		if (outgoing == null) {
			throw new IllegalArgumentException("outgoing server should not be null");
		}
		
		this.title = title;
		this.incoming = incoming;
		this.outgoing = outgoing;
	}
	
	public void begin() throws InterruptedException {
		System.out.println("Starting Services For " + this.title + ".");
		
		Thread incomingThread = new Thread(this.incoming);
		Thread outgoingThread = new Thread(this.outgoing);
		incomingThread.start();
		outgoingThread.start();
		
		incomingThread.join();
		outgoingThread.join();
	}
	
	public void end() {
		this.incoming.closeServer();
		this.outgoing.closeServer();
	}
}
