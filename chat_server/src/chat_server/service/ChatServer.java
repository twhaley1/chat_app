package chat_server.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatServer {
	
	private ExecutorService pool;
	private ServerSocket server;
	private int port;
	
	public ChatServer(int port) {
		this.port = port;
		this.pool = Executors.newFixedThreadPool(6);
		
	}
	
	public void start() throws IOException {
		this.server = new ServerSocket(this.port);
		while (!this.server.isClosed()) {
			Socket client = this.server.accept();
			
		}
	}
	
	public void close() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred when trying to close the server.");
		}
	}
}
