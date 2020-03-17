package com.whaley.chatserver.service.incoming.messagereading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.incoming.IncomingServerHandleInterpreter;
import com.whaley.chatserver.service.incoming.IncomingServerHandleInterpreter.InterpretedData;
import com.whaley.chatserver.socket.ClientEndpoint;

public class MessageReadingService implements Runnable {

	private ClientEndpoint client;
	private SynchronizedQueue<Message> buffer;
	private IncomingServerHandleInterpreter interpreter;
	
	public MessageReadingService(ClientEndpoint client, SynchronizedQueue<Message> buffer, IncomingServerHandleInterpreter interpreter) {
		if (client == null) {
			throw new IllegalArgumentException("client should not be null");
		}
		if (buffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		if (interpreter == null) {
			throw new IllegalArgumentException("interpreter should not be null");
		}
		
		this.client = client;
		this.buffer = buffer;
		this.interpreter = interpreter;
	}
	
	@Override
	public void run() {
		try (InputStreamReader incomingMessages = new InputStreamReader(this.client.getInputStream());
				PrintStream outgoingMessages = new PrintStream(this.client.getOutputStream());
				BufferedReader buffer = new BufferedReader(incomingMessages)) {
			String message = buffer.readLine();
			if (this.interpreter.isValidFormat(message)) {
				InterpretedData data = this.interpreter.extractData(message);
				Message messageData = data.getMessage();
				this.enqueueMessage(messageData);
				System.out.println("Message Received - " + this.client.getInetAddress() + ":" + messageData.getUsername() + " - " + messageData.getContent());
			}
			this.client.close();
		} catch (IOException | InterruptedException e) {
			System.err.println(this.client.getInetAddress() + ": Error - Input/Output Stream Is Closed");
		} 
	}
	
	protected void enqueueMessage(Message data) throws InterruptedException {
		this.buffer.enqueue(data);
	}
	
	protected SynchronizedQueue<Message> getBuffer() {
		return this.buffer;
	}

}
