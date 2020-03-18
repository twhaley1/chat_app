package com.whaley.chatserver.service.outgoing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoing.IncomingMessageInterpreter.InterpretedData;
import com.whaley.chatserver.service.outgoing.messagesending.MessageSendingService;
import com.whaley.chatserver.service.outgoing.messagesending.NotifyingMessageSendingService;
import com.whaley.chatserver.socket.ClientEndpoint;

public class OutgoingMessageServer extends Server {

	private SynchronizedQueue<Message> incomingOutgoingExchangeBuffer;
	private ListeningClients room;
	private MessageSendingService messageService;
	private IncomingMessageInterpreter interpreter;
	
	public OutgoingMessageServer(ServerEndpoint serverConnection, SynchronizedQueue<Message> incomingOutgoingExchangeBuffer) {
		super(serverConnection, Runtime.getRuntime().availableProcessors());
		if (incomingOutgoingExchangeBuffer == null) {
			throw new IllegalArgumentException("exchange buffer should not be null");
		}
		
		this.incomingOutgoingExchangeBuffer = incomingOutgoingExchangeBuffer;
		this.room = new ListeningClients();
		this.messageService = new NotifyingMessageSendingService(this.room, this.incomingOutgoingExchangeBuffer);
		this.interpreter = new IncomingMessageInterpreter();
		this.startRunning(this.messageService);
	}

	@Override
	protected void handleClient(ClientEndpoint client) throws IOException {
		String rawMessage = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
		if (this.interpreter.isValidFormat(rawMessage)) {
			InterpretedData extractedData = this.interpreter.extractData(rawMessage);
			this.performCommand(extractedData.getCommand().toLowerCase(), extractedData.getParameter().toLowerCase(), client);
		}
	}
	
	private void performCommand(String command, String parameter, ClientEndpoint client) throws IOException {
		switch (command) {
			case IncomingMessageInterpreter.ENTER_COMMAND:
				this.room.addClient(parameter, new PrintStream(client.getOutputStream()));
				break;
			case IncomingMessageInterpreter.LEAVE_COMMAND:
				this.room.removeClient(parameter);
				break;
			default:
				System.err.println("Invalid Command Syntax From " + client.getInetAddress());
		}
	}

	@Override
	public void closeServer() {
		this.messageService.closeSendingService();
		super.closeServer();
		this.room.kickClients();
	}
	
	public Collection<String> getUsernamesInRoom() {
		return this.room.getListeningClientUsernames();
	}

}
