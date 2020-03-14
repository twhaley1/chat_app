package chat_server.service;

import java.util.Queue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MessageSendingService implements Runnable {

	private Socket client;
	private Queue<String> buffer;
	
	public MessageSendingService(Socket client, Queue<String> buffer) {
		this.client = client;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		try (InputStreamReader incomingMessages = new InputStreamReader(this.client.getInputStream());
				PrintStream outgoingMessages = new PrintStream(this.client.getOutputStream());
				BufferedReader buffer = new BufferedReader(incomingMessages)) {
			buffer.readLine();
			while (!this.client.isClosed()) {
				String message = this.buffer.remove();
				outgoingMessages.println(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error when performing sending service. The socket may be closed.");
		}
		this.close();
	}
	
	private void close() {
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error when trying to close the sending socket. It may have already been closed.");
		}
	}
	
}
