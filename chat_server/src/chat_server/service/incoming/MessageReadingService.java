package chat_server.service.incoming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Queue;

import chat_server.data.Message;
import chat_server.socket.ClientEndpoint;

public class MessageReadingService implements Runnable {

	private ClientEndpoint client;
	private Queue<Message> buffer;
	
	public MessageReadingService(ClientEndpoint client, Queue<Message> buffer) {
		if (client == null) {
			throw new IllegalArgumentException("client should not be null");
		}
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.client = client;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		try (InputStreamReader incomingMessages = new InputStreamReader(this.client.getInputStream());
				PrintStream outgoingMessages = new PrintStream(this.client.getOutputStream());
				BufferedReader buffer = new BufferedReader(incomingMessages)) {
			String message = buffer.readLine();
			if (message != null) {
				String[] contents = message.split(":");
				if (this.isFormattedCorrectly(contents)) {
					Message data = new Message(contents[0].strip(), contents[1].strip(), Long.parseLong(contents[2].strip()));
					this.enqueueMessage(data);
				}
			}
			this.client.close();
		} catch (IOException | InterruptedException e) {
			System.err.println(this.client.getInetAddress() + ": Error - Input/Output Stream Is Closed");
		} 
	}
	
	protected void enqueueMessage(Message data) throws InterruptedException {
		this.buffer.add(data);
		System.out.println("Message Received - " + this.client.getInetAddress() + ":" + data.getUsername() + " - " + data.getContent());
	}
	
	protected Queue<Message> getBuffer() {
		return this.buffer;
	}
	
	private boolean isFormattedCorrectly(String[] contents) {
		if (contents.length != 3) {
			return false;
		}
		
		String username = contents[0].strip();
		String content = contents[1].strip();
		String timestamp = contents[2].strip();
		
		return !username.isEmpty() && !content.isEmpty() && !timestamp.isEmpty();
	}
}
