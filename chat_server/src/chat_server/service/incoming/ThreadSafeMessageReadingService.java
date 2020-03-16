package chat_server.service.incoming;

import java.util.Queue;

import chat_server.data.Message;
import chat_server.socket.Streamable;

public class ThreadSafeMessageReadingService extends MessageReadingService {

	public ThreadSafeMessageReadingService(Streamable client, Queue<Message> buffer) {
		super(client, buffer);
	}

	@Override
	protected void enqueueMessage(Message data) throws InterruptedException {
		synchronized (this.getBuffer()) {
			super.enqueueMessage(data);
			this.getBuffer().wait();
		}
	}

	
}
