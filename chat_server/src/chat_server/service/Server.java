package chat_server.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chat_server.serversocket.Connectable;
import chat_server.socket.Streamable;

public abstract class Server implements Runnable {
	
	private ExecutorService pool;
	private Connectable serverEndpoint;
	
	protected Server(Connectable serverEndpoint, int numberOfThreads) {
		if (serverEndpoint == null) {
			throw new IllegalArgumentException("server endpoint should not be null");
		}
		if (numberOfThreads < 1) {
			throw new IllegalArgumentException("number of threads should not be less than one");
		}
		
		this.pool = Executors.newFixedThreadPool(numberOfThreads);
		this.serverEndpoint = serverEndpoint;
	}
	
	@Override
	public final void run() {
		try {
			this.start();
		} catch (IOException e) {
			this.close();
		}
	}

	private void start() throws IOException {
		while (!this.serverEndpoint.isClosed()) {
			Streamable clientEndpoint = this.serverEndpoint.accept();
			try {
				this.handle(clientEndpoint);
			} catch (IOException e) {
				System.err.println(clientEndpoint.getInetAddress() + ": Client Endpoint Closed.");
			}
		}
	}
	
	protected abstract void handle(Streamable client) throws IOException;
	
	protected void execute(Runnable task) {
		this.pool.execute(task);
	}
	
	public void close() {
		try {
			this.serverEndpoint.close();
			this.pool.shutdown();
		} catch (IOException e) {
			System.err.println("An IOException Was Thrown When Closing Down A Server.");
			System.err.println("Log: " + e.getMessage());
		}
	}
	
	public final boolean isClosed() {
		return this.serverEndpoint.isClosed();
	}
}
