package com.whaley.chatserver.service.outgoing;

import java.util.List;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;

public class NotifyingMessageSendingService extends MessageSendingService {

	public NotifyingMessageSendingService(ListeningRoom clients, SynchronizedQueue<Message> buffer) {
		super(clients, buffer);
	}

	@Override
	protected List<Message> dequeueMessages() {
		return this.getBuffer().transferAndNotify();
	}
	
}
