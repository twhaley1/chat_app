package com.whaley.chatserver.service.incoming;

import java.io.IOException;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.incoming.messagereading.WaitingMessageReadingService;
import com.whaley.chatserver.socket.ClientEndpoint;

public class IncomingMessageServer extends Server {

	private SynchronizedQueue<Message> buffer;
	private IncomingServerHandleInterpreter interpreter;
	
	public IncomingMessageServer(ServerEndpoint endpoint, SynchronizedQueue<Message> buffer) {
		super(endpoint, Runtime.getRuntime().availableProcessors());
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.buffer = buffer;
		this.interpreter = new IncomingServerHandleInterpreter();
	}

	@Override
	protected void handle(ClientEndpoint client) throws IOException {
		this.execute(this.createReadingService(client));
	}

	protected Runnable createReadingService(ClientEndpoint client) {
		return new WaitingMessageReadingService(client, this.buffer, this.interpreter);
	}
	
	protected final SynchronizedQueue<Message> getBuffer() {
		return this.buffer;
	}
}