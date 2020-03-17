package com.whaley.chatserver.service.outgoing.messagesending;

import java.util.List;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.outgoing.ListeningRoom;

public class MessageSendingService implements Runnable {

	private ListeningRoom room;
	private SynchronizedQueue<Message> buffer;
	
	private volatile boolean isRunning;
	
	public MessageSendingService(ListeningRoom room, SynchronizedQueue<Message> buffer) {
		if (room == null) {
			throw new IllegalArgumentException("room should not be null");
		}
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.room = room;
		this.buffer = buffer;
		this.isRunning = false;
	}
	
	@Override
	public final void run() {
		this.isRunning = true;
		while (this.isRunning()) {
			if (this.containsMessages()) {
				List<Message> bufferedItems = this.dequeueMessages();
				for (Message message : bufferedItems) {
					if (message != null) {
						this.room.sendToListeners(message);
					}
				}
			}
		}
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public boolean containsMessages() {
		return !this.buffer.isEmpty();
	}
	
	protected List<Message> dequeueMessages() {
		return this.buffer.transfer();
	}
	
	protected final SynchronizedQueue<Message> getBuffer() {
		return this.buffer;
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
	
}
