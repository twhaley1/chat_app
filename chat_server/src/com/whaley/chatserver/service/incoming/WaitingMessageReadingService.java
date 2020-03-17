package com.whaley.chatserver.service.incoming;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.socket.ClientEndpoint;

public class WaitingMessageReadingService extends MessageReadingService {

	public WaitingMessageReadingService(ClientEndpoint client, SynchronizedQueue<Message> buffer) {
		super(client, buffer);
	}

	@Override
	protected void enqueueMessage(Message data) throws InterruptedException {
		this.getBuffer().enqueueAndWait(data);
	}

}
