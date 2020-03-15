package chat_server.service.incoming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Queue;

import chat_server.data.Message;

public class MessageReadingService implements Runnable {

	private Socket client;
	private Queue<Message> buffer;
	
	public MessageReadingService(Socket client, Queue<Message> buffer) {
		this.client = client;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		try (InputStreamReader incomingMessages = new InputStreamReader(this.client.getInputStream());
				PrintStream outgoingMessages = new PrintStream(this.client.getOutputStream());
				BufferedReader buffer = new BufferedReader(incomingMessages)) {
			String message = buffer.readLine();
			String[] contents = message.split(":");
			synchronized (this.buffer) {
				this.buffer.add(new Message(contents[0], contents[1], Long.parseLong(contents[2])));
				this.buffer.wait();
			}
			this.client.close();
		} catch (IOException | InterruptedException e) {
			return;
		}
	}
}
