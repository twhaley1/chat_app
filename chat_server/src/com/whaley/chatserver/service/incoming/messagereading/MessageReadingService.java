package com.whaley.chatserver.service.incoming.messagereading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.incoming.IncomingMessageInterpreter;
import com.whaley.chatserver.service.incoming.IncomingMessageInterpreter.InterpretedData;
import com.whaley.chatserver.socket.ClientEndpoint;

public class MessageReadingService implements Runnable {

	private ClientEndpoint clientConnection;
	private SynchronizedQueue<Message> incomingOutgoingExchangeBuffer;
	private IncomingMessageInterpreter messageInterpreter;
	
	public MessageReadingService(ClientEndpoint clientConnection, SynchronizedQueue<Message> incomingOutgoingExchangeBuffer) {
		if (clientConnection == null) {
			throw new IllegalArgumentException("client should not be null");
		}
		if (incomingOutgoingExchangeBuffer == null) {
			throw new IllegalArgumentException("buffer should not be null");
		}
		
		this.clientConnection = clientConnection;
		this.incomingOutgoingExchangeBuffer = incomingOutgoingExchangeBuffer;
		this.messageInterpreter = new IncomingMessageInterpreter();
	}
	
	@Override
	public void run() {
		try (InputStreamReader fromClient = new InputStreamReader(this.clientConnection.getInputStream());
				BufferedReader fromClientBuffer = new BufferedReader(fromClient)) {
			
			String rawMessageFromClient = fromClientBuffer.readLine();
			if (this.messageInterpreter.isValidFormat(rawMessageFromClient)) {
				InterpretedData extractedData = this.messageInterpreter.extractData(rawMessageFromClient);
				Message message = extractedData.getMessage();
				
				System.out.println("Message Received - " + this.clientConnection.getInetAddress() + ":" + message.getUsername() + " - " + message.getMessageContent());
				this.enqueueOnExchangeBuffer(message);
			}
			this.clientConnection.close();
		} catch (IOException | InterruptedException e) {
			System.err.println(this.clientConnection.getInetAddress() + ": Error - Input/Output Stream Is Closed");
		} 
	}
	
	protected void enqueueOnExchangeBuffer(Message data) throws InterruptedException {
		this.incomingOutgoingExchangeBuffer.enqueue(data);
	}
	
	protected SynchronizedQueue<Message> getIncomingOutgoingExchangeBuffer() {
		return this.incomingOutgoingExchangeBuffer;
	}

}
