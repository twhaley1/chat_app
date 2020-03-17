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
import com.whaley.chatserver.service.outgoing.OutgoingServerHandleInterpreter.InterpretedData;
import com.whaley.chatserver.service.outgoing.messagesending.MessageSendingService;
import com.whaley.chatserver.service.outgoing.messagesending.NotifyingMessageSendingService;
import com.whaley.chatserver.socket.ClientEndpoint;

public class OutgoingMessageServer extends Server {

	private SynchronizedQueue<Message> buffer;
	private ListeningRoom room;
	private MessageSendingService messageService;
	private OutgoingServerHandleInterpreter interpreter;
	
	public OutgoingMessageServer(ServerEndpoint endpoint, SynchronizedQueue<Message> buffer) {
		super(endpoint, Runtime.getRuntime().availableProcessors());
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.buffer = buffer;
		this.room = new ListeningRoom();
		this.messageService = new NotifyingMessageSendingService(this.room, this.buffer);
		this.interpreter = new OutgoingServerHandleInterpreter();
		this.execute(this.messageService);
	}

	@Override
	protected void handle(ClientEndpoint client) throws IOException {
		String command = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
		if (this.interpreter.isValidFormat(command)) {
			InterpretedData data = this.interpreter.extractData(command);
			this.performCommand(data.getCommand(), data.getParameter(), client);
		}
	}
	
	private void performCommand(String command, String parameter, ClientEndpoint client) throws IOException {
		if (command.toLowerCase().equals("enter")) {
			this.room.assignListener(parameter, new PrintStream(client.getOutputStream()));
		} else if (command.toLowerCase().equals("leave")) {
			this.room.removeListener(parameter);
		} else {
			System.err.println("Invalid Command Syntax From " + client.getInetAddress());
		}
	}

	@Override
	public void closeServer() {
		this.messageService.shutdown();
		super.closeServer();
		this.room.kickListeners();
	}
	
	public Collection<String> getUsernamesInRoom() {
		return this.room.getListeningUsernames();
	}

}
