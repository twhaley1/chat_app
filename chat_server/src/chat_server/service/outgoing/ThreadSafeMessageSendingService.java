package chat_server.service.outgoing;

import java.io.PrintStream;
import java.util.Map;
import java.util.Queue;

import chat_server.data.Message;

public class ThreadSafeMessageSendingService extends MessageSendingService {

	public ThreadSafeMessageSendingService(Map<String, PrintStream> clients, Map<String, Long> tracker,
			Queue<Message> buffer) {
		super(clients, tracker, buffer);
	}

	
	
	@Override
	public boolean containsMessages() {
		synchronized (this.getBuffer()) {
			return super.containsMessages();
		}
	}

	@Override
	protected void clearBuffer() {
		synchronized (this.getBuffer()) {
			super.clearBuffer();
			this.getBuffer().notify();
		}
	}

	@Override
	protected void assignTrackingTimestamp(Message message) {
		synchronized (this.getClientTracker()) {
			super.assignTrackingTimestamp(message);
		}
	}

	@Override
	protected void sendMessageToClients(Message message) {
		synchronized (this.getClientStreams()) {
			super.sendMessageToClients(message);
		}
	}

	
}
