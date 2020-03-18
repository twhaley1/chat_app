package com.whaley.chatserver.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.socket.ClientEndpoint;

public abstract class Server implements Runnable {
	
	private static final int MINIMUM_NUMBER_OF_THREADS = 1;
	
	private ExecutorService threadPool;
	private ServerEndpoint serverConnection;
	
	protected Server(ServerEndpoint serverConnection, int numberOfThreads) {
		if (serverConnection == null) {
			throw new IllegalArgumentException("server connection should not be null");
		}
		if (numberOfThreads < MINIMUM_NUMBER_OF_THREADS) {
			throw new IllegalArgumentException("number of threads should not be less than one");
		}
		
		this.threadPool = Executors.newFixedThreadPool(numberOfThreads);
		this.serverConnection = serverConnection;
	}
	
	@Override
	public void run() {
		try {
			this.startServer();
		} catch (IOException e) {
			this.closeServer();
		}
	}

	private void startServer() throws IOException {
		while (!this.serverConnection.isClosed()) {
			ClientEndpoint clientConnection = this.serverConnection.acceptClientEndpoint();
			try {
				this.handleClient(clientConnection);
			} catch (IOException e) {
				System.err.println(clientConnection.getInetAddress() + ": Client Endpoint Closed.");
			}
		}
	}
	
	protected abstract void handleClient(ClientEndpoint client) throws IOException;
	
	public void closeServer() {
		try {
			this.threadPool.awaitTermination(50, TimeUnit.MILLISECONDS);
			this.serverConnection.closeServerEndpoint();
		} catch (IOException | InterruptedException e) {
			System.err.println("An IOException Was Thrown When Closing Down A Server.");
		}
	}
	
	public boolean isClosed() {
		return this.serverConnection.isClosed();
	}
	
	protected void startRunning(Runnable task) {
		this.threadPool.execute(task);
	}
	
}
