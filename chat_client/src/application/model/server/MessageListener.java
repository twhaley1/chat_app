package application.model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MessageListener implements Runnable {

	private StringProperty message;
	
	public MessageListener() {
		this.message = new SimpleStringProperty();
	}
	
	@Override
	public void run() {
		try (Socket sock = new Socket("localhost", 4235);
				PrintStream outgoingMessages = new PrintStream(sock.getOutputStream());
				InputStreamReader incomingMessages = new InputStreamReader(sock.getInputStream());
				BufferedReader incomingBuffer = new BufferedReader(incomingMessages)) {
			
			outgoingMessages.println("listening");
			String result = null;
			while (!(result = incomingBuffer.readLine()).equals("5449815165132468")) {
				this.message.setValue(result);
			}
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public StringProperty messageProperty() {
		return this.message;
	}

}
