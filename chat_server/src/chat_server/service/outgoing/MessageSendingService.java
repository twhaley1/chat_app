package chat_server.service.outgoing;

import java.util.Map;
import java.util.Queue;

import chat_server.data.Message;

import java.io.PrintStream;

public class MessageSendingService implements Runnable {

	private Map<String, PrintStream> clients;
	private Map<String, Long> tracker;
	private Queue<Message> buffer;
	
	private volatile boolean isRunning;
	
	public MessageSendingService(Map<String, PrintStream> clients, Map<String, Long> tracker, Queue<Message> buffer) {
		if (clients == null) {
			throw new IllegalArgumentException("clients should not be null");
		}
		if (tracker == null) {
			throw new IllegalArgumentException("tracker should not be null");
		}
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.clients = clients;
		this.tracker = tracker;
		this.buffer = buffer;
		this.isRunning = false;
	}
	
	@Override
	public final void run() {
		this.isRunning = true;
		while (this.isRunning()) {
			if (this.containsMessages()) {
				this.dequeueMessages();
			}
		}
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public boolean containsMessages() {
		synchronized (this.buffer) {
			return !this.buffer.isEmpty();
		}
	}
	
	protected void dequeueMessages() {
		while (this.containsMessages()) {
			Message message = this.buffer.remove();
			if (message != null) {
				this.assignTrackingTimestamp(message);
				this.sendMessageToClients(message);
			}
		}
	}
	
	protected final Queue<Message> getBuffer() {
		return this.buffer;
	}
	
	private void assignTrackingTimestamp(Message message) {
		synchronized (this.tracker) {
			this.tracker.put(message.getUsername(), message.getTimestamp());
		}
	}
	
	private void sendMessageToClients(Message message) {
		synchronized (this.clients) {
			for (PrintStream client : this.clients.values()) {
				client.println(message.getUsername() + ": " + message.getContent());
			}
		}
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
	
}
