package com.whaley.chatserver.chat;

import com.whaley.chatserver.service.Server;

public class Chat {

	private String title;
	
	private Server incomingMessageServer;
	private Server outgoingMessageServer;
	
	public Chat(String title, Server incomingMessageServer, Server outgoingMessageServer) {
		if (title == null) {
			throw new IllegalArgumentException("title should not be null");
		}
		if (incomingMessageServer == null) {
			throw new IllegalArgumentException("incoming server should not be null");
		}
		if (outgoingMessageServer == null) {
			throw new IllegalArgumentException("outgoing server should not be null");
		}
		
		this.title = title;
		this.incomingMessageServer = incomingMessageServer;
		this.outgoingMessageServer = outgoingMessageServer;
	}
	
	public void startChat() throws InterruptedException {
		System.out.println("Starting Services For " + this.title + ".");
		
		Thread incomingMessageServerThread = new Thread(this.incomingMessageServer);
		Thread outgoingMessageServerThread = new Thread(this.outgoingMessageServer);
		
		incomingMessageServerThread.start();
		outgoingMessageServerThread.start();
		
		incomingMessageServerThread.join();
		outgoingMessageServerThread.join();
	}
	
	public void endChat() {
		this.incomingMessageServer.closeServer();
		this.outgoingMessageServer.closeServer();
	}
}
