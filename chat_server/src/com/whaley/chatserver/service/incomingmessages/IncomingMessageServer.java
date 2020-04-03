package com.whaley.chatserver.service.incomingmessages;

import java.io.IOException;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.incomingmessages.messagereading.MessageReadingService;
import com.whaley.chatserver.service.incomingmessages.messagereading.WaitingMessageReadingService;
import com.whaley.chatserver.socket.ClientEndpoint;

public class IncomingMessageServer extends Server {

	private SynchronizedQueue<Message> incomingOutgoingExchangeBuffer;
	
	public IncomingMessageServer(ServerEndpoint serverConnection, SynchronizedQueue<Message> incomingOutgoingExchangeBuffer) {
		super(serverConnection, Runtime.getRuntime().availableProcessors());
		if (incomingOutgoingExchangeBuffer == null) {
			throw new IllegalArgumentException("exchange buffer should not be null");
		}
		
		this.incomingOutgoingExchangeBuffer = incomingOutgoingExchangeBuffer;
	}

	@Override
	protected void handleClient(ClientEndpoint client) throws IOException {
		this.startRunning(this.createReadingService(client));
	}

	protected MessageReadingService createReadingService(ClientEndpoint client) {
		return new WaitingMessageReadingService(client, this.incomingOutgoingExchangeBuffer);
	}
	
	protected final SynchronizedQueue<Message> getIncomingOutgoingExchangeBuffer() {
		return this.incomingOutgoingExchangeBuffer;
	}
}
