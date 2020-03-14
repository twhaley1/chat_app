package chat_server.service.incoming;

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
			String message = buffer.readLine();
			synchronized (this.buffer) {
				this.buffer.add(message);
				this.buffer.wait();
			}
			this.client.close();
		} catch (IOException | InterruptedException e) {
			return;
		}
	}
}
