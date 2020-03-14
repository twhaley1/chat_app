package chat_server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import chat_server.users.RegisteredUsers;

public class LoginService implements Runnable {

	private Socket client;
	private RegisteredUsers register;
	
	public LoginService(Socket client, RegisteredUsers register) {
		this.client = client;
		this.register = register;
	}
	
	@Override
	public void run() {
		try (InputStreamReader incomingMessages = new InputStreamReader(this.client.getInputStream());
				PrintStream outgoingMessages = new PrintStream(this.client.getOutputStream());
				BufferedReader buffer = new BufferedReader(incomingMessages)) {
			String login = buffer.readLine();
			String[] credentials = login.split(":");
			boolean isValid = this.register.login(credentials[0], credentials[1]);
			outgoingMessages.println(isValid);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error when performing login service. The socket may be closed.");
		}
		this.close();
	}
	
	private void close() {
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an error when trying to close the login socket. It may have already been closed.");
		}
	}

}
