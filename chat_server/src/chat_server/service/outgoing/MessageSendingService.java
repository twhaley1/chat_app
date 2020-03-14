package chat_server.service.outgoing;

import java.util.Collection;
import java.util.Queue;
import java.io.PrintStream;

public class MessageSendingService implements Runnable {

	private Collection<PrintStream> clients;
	private Queue<String> buffer;
	
	private volatile boolean isRunning;
	
	public MessageSendingService(Collection<PrintStream> clients, Queue<String> buffer) {
		this.clients = clients;
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
						String message = this.buffer.remove();
						synchronized (this.clients) {
							for (PrintStream client : this.clients) {
								if (message != null) {
									client.println(message);
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
