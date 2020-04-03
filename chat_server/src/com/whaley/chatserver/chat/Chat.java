package com.whaley.chatserver.chat;

import com.whaley.chatserver.service.Server;

/**
 * Chat is the main controller for the chat server. It takes two servers,
 * an incoming server and an outgoing server. The incoming server is 
 * responsible for handling all incoming messages. The outgoing server is
 * responsible for maintaining a list of "subscribers"; then, when a message
 * is sent to the incoming server, all subscribers of the outgoing server will
 * receive the message.
 * 
 * @author Thomas Whaley
 *
 */
public class Chat {

	private String title;
	
	private Server incomingMessageServer;
	private Server outgoingMessageServer;
	private Server updatingServer;
	
	/**
	 * Creates a new Chat with the specified title, incoming server, and outgoing server.
	 * The incoming server listens for messages while the outgoing server sends messages.
	 * 
	 * @precondition title != null && incomingMessageServer != null && outgoingMessageServer != null
	 * 
	 * @param title the chat title.
	 * @param incomingMessageServer the server responsible for listening for incoming messages.
	 * @param outgoingMessageServer the server responsible for listening for outgoing messages.
	 */
	public Chat(String title, Server incomingMessageServer, Server outgoingMessageServer, Server updatingServer) {
		if (title == null) {
			throw new IllegalArgumentException("title should not be null");
		}
		if (incomingMessageServer == null) {
			throw new IllegalArgumentException("incoming server should not be null");
		}
		if (outgoingMessageServer == null) {
			throw new IllegalArgumentException("outgoing server should not be null");
		}
		if (updatingServer == null) {
			throw new IllegalArgumentException("updating server should not be null");
		}
		
		this.title = title;
		this.incomingMessageServer = incomingMessageServer;
		this.outgoingMessageServer = outgoingMessageServer;
		this.updatingServer = updatingServer;
	}
	
	/**
	 * Starts the chat. Both the incoming and outgoing server are started on their own
	 * thread and then joined.
	 * 
	 * @throws InterruptedException if any of the server threads are interrupted.
	 */
	public void startChat() throws InterruptedException {
		System.out.println("Starting Services For " + this.title + ".");
		
		Thread incomingMessageServerThread = new Thread(this.incomingMessageServer);
		Thread outgoingMessageServerThread = new Thread(this.outgoingMessageServer);
		Thread updatingServerThread = new Thread(this.updatingServer);
		
		incomingMessageServerThread.start();
		outgoingMessageServerThread.start();
		updatingServerThread.start();
		
		incomingMessageServerThread.join();
		outgoingMessageServerThread.join();
		updatingServerThread.join();
	}
	
	/**
	 * Ends the chat. Both servers and all of their resources are closed and
	 * shutdown appropriately.
	 */
	public void endChat() {
		this.incomingMessageServer.closeServer();
		this.outgoingMessageServer.closeServer();
		this.updatingServer.closeServer();
	}
}
