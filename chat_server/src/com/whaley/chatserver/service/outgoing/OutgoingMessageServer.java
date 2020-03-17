package com.whaley.chatserver.service.outgoing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.socket.ClientEndpoint;

public class OutgoingMessageServer extends Server {

	private SynchronizedQueue<Message> buffer;
	private ListeningRoom room;
	private MessageSendingService messageService;
	
	public OutgoingMessageServer(ServerEndpoint endpoint, SynchronizedQueue<Message> buffer) {
		super(endpoint, Runtime.getRuntime().availableProcessors());
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.buffer = buffer;
		this.room = new ListeningRoom();
		this.messageService = new NotifyingMessageSendingService(this.room, this.buffer);
		this.execute(this.messageService);
	}

	@Override
	protected void handle(ClientEndpoint client) throws IOException {
		String command = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
		String[] tokens = command.split(" ");
		if (tokens[0].toLowerCase().equals("enter")) {
			this.room.assignListener(tokens[1], new PrintStream(client.getOutputStream()));
		} else if (tokens[0].toLowerCase().equals("leave")) {
			this.room.removeListener(tokens[1]);
		} else {
			
		}
	}

	@Override
	public void close() {
		this.messageService.shutdown();
		super.close();
		this.room.kickListeners();
	}
	
	public Collection<String> getUsernamesInRoom() {
		return this.room.getListeningUsernames();
	}

}
