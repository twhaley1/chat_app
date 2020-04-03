package com.whaley.chatserver.service.outgoingmessages.messagesending;

import java.util.List;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoingmessages.ListeningClients;

public class NotifyingMessageSendingService extends MessageSendingService {

	public NotifyingMessageSendingService(ListeningClients clients, SynchronizedQueue<Message> incomingOutgoingExchangeBuffer) {
		super(clients, incomingOutgoingExchangeBuffer);
	}

	@Override
	protected List<Message> dequeueMessageBuffer() {
		return this.getIncomingOutgoingExchangeBuffer().transferAndNotify();
	}
	
}
