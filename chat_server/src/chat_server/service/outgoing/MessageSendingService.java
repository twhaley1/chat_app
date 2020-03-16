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
				this.clearBuffer();
			}
		}
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public boolean containsMessages() {
		return !this.getBuffer().isEmpty();
	}
	
	protected void clearBuffer() {
		while (this.containsMessages()) {
			Message message = this.getBuffer().remove();
			if (message != null) {
				this.assignTrackingTimestamp(message);
				this.sendMessageToClients(message);
			}
		}
	}
	
	protected void assignTrackingTimestamp(Message message) {
		this.getClientTracker().put(message.getUsername(), message.getTimestamp());
	}
	
	protected void sendMessageToClients(Message message) {
		for (PrintStream client : this.getClientStreams().values()) {
			client.println(message.getUsername() + ": " + message.getContent());
		}
	}
	
	protected final Queue<Message> getBuffer() {
		return this.buffer;
	}
	
	protected final Map<String, PrintStream> getClientStreams() {
		return this.clients;
	}
	
	protected final Map<String, Long> getClientTracker() {
		return this.tracker;
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
	
}
