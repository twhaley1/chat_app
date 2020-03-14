package chat_server_client_TEST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		try (Socket sock = new Socket("localhost", 4225);
				PrintStream outgoingMessages = new PrintStream(sock.getOutputStream());
				InputStreamReader incomingMessages = new InputStreamReader(sock.getInputStream());
				BufferedReader incomingBuffer = new BufferedReader(incomingMessages);
				InputStreamReader keyboardInput = new InputStreamReader(System.in);
				BufferedReader keyboardBuffer = new BufferedReader(keyboardInput)) {
			System.out.print("Username: ");
			String username = keyboardBuffer.readLine();
			System.out.print("Password: ");
			String password = keyboardBuffer.readLine();
			
			outgoingMessages.println(username + ":" + password);
			String result = incomingBuffer.readLine();
			System.out.println("Login Successful? " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
