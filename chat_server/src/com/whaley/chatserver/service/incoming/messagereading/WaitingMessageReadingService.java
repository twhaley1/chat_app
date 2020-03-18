package com.whaley.chatserver.service.incoming.messagereading;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.socket.ClientEndpoint;

public class WaitingMessageReadingService extends MessageReadingService {

	public WaitingMessageReadingService(ClientEndpoint clientConnection, SynchronizedQueue<Message> incomingOutgoingExchangeBuffer) {
		super(clientConnection, incomingOutgoingExchangeBuffer);
	}

	@Override
	protected void enqueueOnExchangeBuffer(Message data) throws InterruptedException {
		this.getIncomingOutgoingExchangeBuffer().enqueueAndWait(data);
	}

}
