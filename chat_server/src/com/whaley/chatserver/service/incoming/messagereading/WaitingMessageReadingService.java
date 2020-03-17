package com.whaley.chatserver.service.incoming.messagereading;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.incoming.IncomingServerHandleInterpreter;
import com.whaley.chatserver.socket.ClientEndpoint;

public class WaitingMessageReadingService extends MessageReadingService {

	public WaitingMessageReadingService(ClientEndpoint client, SynchronizedQueue<Message> buffer, IncomingServerHandleInterpreter interpreter) {
		super(client, buffer, interpreter);
	}

	@Override
	protected void enqueueMessage(Message data) throws InterruptedException {
		this.getBuffer().enqueueAndWait(data);
	}

}
