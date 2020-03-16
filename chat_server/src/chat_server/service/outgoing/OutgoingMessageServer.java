package chat_server.service.outgoing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import chat_server.data.Message;
import chat_server.serversocket.Connectable;
import chat_server.service.Server;
import chat_server.socket.Streamable;

public class OutgoingMessageServer extends Server {

	private Queue<Message> buffer;
	private Map<String, PrintStream> room;
	private Map<String, Long> tracker;
	
	private MessageSendingService messageService;
	private UserTrackingService trackingService;
	
	public OutgoingMessageServer(Connectable endpoint, Queue<Message> buffer) {
		super(endpoint, Runtime.getRuntime().availableProcessors());
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.buffer = buffer;
		
		this.room = Collections.synchronizedMap(new HashMap<String, PrintStream>());
		this.tracker = Collections.synchronizedMap(new HashMap<String, Long>());
		
		this.messageService = new ThreadSafeMessageSendingService(this.room, this.tracker, this.buffer);
		this.trackingService = new ThreadSafeUserTrackingService(this.room, this.tracker, UserTrackingService.TIMEOUT_ONE_MINUTE);
		
		this.execute(this.messageService);
		this.execute(this.trackingService);
	}

	@Override
	protected void handle(Streamable client) throws IOException {
		String username = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
		synchronized (this.room) {
			this.room.put(username, new PrintStream(client.getOutputStream()));
		}
		synchronized (this.tracker) {
			this.tracker.put(username, System.currentTimeMillis());
		}
	}

	@Override
	public void close() {
		super.close();
		this.messageService.shutdown();
		for (PrintStream client : this.room.values()) {
			client.close();
		}
	}
}
