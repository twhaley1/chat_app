package chat_server.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server implements Runnable {
	
	private ExecutorService pool;
	private ServerSocket server;
	private int port;
	
	protected Server(int port) {
		this.port = port;
		this.pool = Executors.newFixedThreadPool(6);
	}
	
	@Override
	public void run() {
		try {
			this.start();
		} catch (IOException e) {
			this.close();
		}
	}

	public void start() throws IOException {
		this.server = new ServerSocket(this.port);
		while (!this.server.isClosed()) {
			Socket client = this.server.accept();
			this.handle(client);
		}
	}
	
	protected abstract void handle(Socket client);
	
	protected void execute(Runnable task) {
		this.pool.execute(task);
	}
	
	public void close() {
		try {
			this.server.close();
			this.pool.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred when trying to close the server.");
		}
	}
	
}
