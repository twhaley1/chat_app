package chat_server.service.outgoing;

import java.io.PrintStream;
import java.util.Map;
import java.util.Queue;

import chat_server.data.Message;

public class NotifyingMessageSendingService extends MessageSendingService {

	public NotifyingMessageSendingService(Map<String, PrintStream> clients, Map<String, Long> tracker,
			Queue<Message> buffer) {
		super(clients, tracker, buffer);
	}

	@Override
	protected void dequeueMessages() {
		synchronized (this.getBuffer()) {
			super.dequeueMessages();
			this.getBuffer().notify();
		}
	}
	
}
