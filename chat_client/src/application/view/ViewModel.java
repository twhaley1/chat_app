package application.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import application.model.server.MessageListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class ViewModel {

	private static ViewModel viewmodel = null;
	
	public static ViewModel get() {
		if (viewmodel == null) {
			viewmodel = new ViewModel();
		}
		return viewmodel;
	}
	
	private StringProperty usernameProperty;
	private StringProperty chatProperty;
	private StringProperty inputProperty;
	
	private MessageListener listener;
	private Thread listeningThread;
	
	private ViewModel() {
		this.usernameProperty = new SimpleStringProperty();
		this.chatProperty = new SimpleStringProperty("");
		this.inputProperty = new SimpleStringProperty();
	}
	
	public void makeConnection() throws UnknownHostException, IOException {
		this.listener = new MessageListener(this.usernameProperty.getValue());
		listener.messageProperty().addListener((observable, oldValue, newValue) -> {
			String previous = this.chatProperty.getValue() + System.lineSeparator();
			this.chatProperty.setValue(previous + newValue);
		});
		this.listeningThread = new Thread(listener);
		this.listeningThread.start();
	}
	
	public void send() {
		try (Socket sock = new Socket("localhost", 4230);
				PrintStream outgoingMessages = new PrintStream(sock.getOutputStream());
				InputStreamReader incomingMessages = new InputStreamReader(sock.getInputStream());
				BufferedReader incomingBuffer = new BufferedReader(incomingMessages)) {
			String message = this.usernameProperty.getValue() + ":" 
				+ this.inputProperty.getValue().replaceAll(System.lineSeparator(), "") + ":" 
				+ System.currentTimeMillis();
			outgoingMessages.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() throws InterruptedException {
		if (this.listener != null) {
			this.listener.shutdown();
			this.listeningThread.join();
		}
	}
	
	public StringProperty usernameProperty() {
		return this.usernameProperty;
	}
	
	public StringProperty chatProperty() {
		return this.chatProperty;
	}
	
	public StringProperty inputProperty() {
		return this.inputProperty;
	}

}
