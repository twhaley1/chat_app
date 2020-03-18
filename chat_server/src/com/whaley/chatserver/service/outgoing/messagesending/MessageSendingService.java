package com.whaley.chatserver.service.outgoing.messagesending;

import java.util.List;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoing.ListeningClients;

public class MessageSendingService implements Runnable {

	private ListeningClients room;
	private SynchronizedQueue<Message> incomingOutgoingExchangeBuffer;
	
	private volatile boolean isRunning;
	
	public MessageSendingService(ListeningClients room, SynchronizedQueue<Message> incomingOutgoingExchangeBuffer) {
		if (room == null) {
			throw new IllegalArgumentException("room should not be null");
		}
		if (incomingOutgoingExchangeBuffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.room = room;
		this.incomingOutgoingExchangeBuffer = incomingOutgoingExchangeBuffer;
		this.isRunning = false;
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		while (this.isRunning()) {
			if (this.bufferContainsMessages()) {
				List<Message> messages = this.dequeueMessageBuffer();
				for (Message message : messages) {
					if (message != null) {
						this.room.sendToClients(message);
					}
				}
			}
		}
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public boolean bufferContainsMessages() {
		return !this.incomingOutgoingExchangeBuffer.isEmpty();
	}
	
	protected List<Message> dequeueMessageBuffer() {
		return this.incomingOutgoingExchangeBuffer.transfer();
	}
	
	protected final SynchronizedQueue<Message> getIncomingOutgoingExchangeBuffer() {
		return this.incomingOutgoingExchangeBuffer;
	}
	
	public void closeSendingService() {
		this.isRunning = false;
	}
	
}
