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
		this.clients = clients;
		this.tracker = tracker;
		this.buffer = buffer;
		this.isRunning = false;
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		while (this.isRunning) {
			if (!this.buffer.isEmpty()) {
				synchronized (this.buffer) {
					while (!this.buffer.isEmpty()) {
						Message message = this.buffer.remove();
						synchronized (this.tracker) {
							this.tracker.put(message.getUsername(), System.currentTimeMillis());
						}
						synchronized (this.clients) {
							for (PrintStream client : this.clients.values()) {
								if (message != null) {
									client.println(message.getUsername() + ": " + message.getContent());
								}
							}
						}
					}
					this.buffer.notify();
				}
			}
		}
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
	
}
