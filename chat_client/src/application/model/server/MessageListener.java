package application.model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MessageListener implements Runnable {

	private StringProperty message;
	private ObjectProperty<Exception> error;
	
	private String username;
	private Socket sock;
	private PrintStream outgoingMessages;
	private BufferedReader incomingBuffer;
	
	private boolean isListening;
	
	public MessageListener(String username) throws UnknownHostException, IOException {
		this.message = new SimpleStringProperty();
		this.error = new SimpleObjectProperty<Exception>();
		this.isListening = false;
		this.username = username;
		
		this.sock = new Socket("localhost", 4235);
		this.outgoingMessages = new PrintStream(sock.getOutputStream());
		this.incomingBuffer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	@Override
	public void run() {
		this.isListening = true;
		this.outgoingMessages.println(this.username);
		while (this.isListening) {
			try {
				String result = this.incomingBuffer.readLine();
				this.message.setValue(result);
			} catch (IOException | NullPointerException e) {
				this.error.setValue(e);
				this.isListening = false;
			}
		}
	}
	
	public void shutdown() {
		try {
			this.sock.close();
		} catch (IOException e) {
			this.error.setValue(e);
		}
	}
	
	public StringProperty messageProperty() {
		return this.message;
	}
	
	public ObjectProperty<Exception> exceptionProperty() {
		return this.error;
	}

}
