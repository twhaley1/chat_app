package chat_server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Queue;

public class MessageReadingService implements Runnable {

	private Socket client;
	private Queue<String> buffer;
	
	public MessageReadingService(Socket client, Queue<String> buffer) {
		this.client = client;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		try (InputStreamReader incomingMessages = new InputStreamReader(this.client.getInputStream());
				PrintStream outgoingMessages = new PrintStream(this.client.getOutputStream());
				BufferedReader buffer = new BufferedReader(incomingMessages)) {
			String login = buffer.readLine();
			this.buffer.add(login);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error when performing reading service. The socket may be closed.");
		}
		this.close();
	}

	private void close() {
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error when trying to close the reading socket. It may have already been closed.");
		}
	}
}
