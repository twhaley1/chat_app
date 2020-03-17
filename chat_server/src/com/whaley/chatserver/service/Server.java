package com.whaley.chatserver.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.socket.ClientEndpoint;

public abstract class Server implements Runnable {
	
	private ExecutorService pool;
	private ServerEndpoint serverEndpoint;
	
	protected Server(ServerEndpoint serverEndpoint, int numberOfThreads) {
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
			ClientEndpoint clientEndpoint = this.serverEndpoint.accept();
			try {
				this.handle(clientEndpoint);
			} catch (IOException e) {
				System.err.println(clientEndpoint.getInetAddress() + ": Client Endpoint Closed.");
			}
		}
	}
	
	protected abstract void handle(ClientEndpoint client) throws IOException;
	
	protected void execute(Runnable task) {
		this.pool.execute(task);
	}
	
	public void close() {
		try {
			this.pool.awaitTermination(50, TimeUnit.MILLISECONDS);
			this.serverEndpoint.close();
		} catch (IOException | InterruptedException e) {
			System.err.println("An IOException Was Thrown When Closing Down A Server.");
		}
	}
	
	public final boolean isClosed() {
		return this.serverEndpoint.isClosed();
	}
}
