package chat_server_client_TEST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Main {

	public static void main(String[] args) throws IOException {
		Thread readThread = new Thread(new IncomingWatch());
		readThread.start();
		chat("Anon");
	}

	private static void chat(String username) {
		try (InputStreamReader keyboardInput = new InputStreamReader(System.in);
				BufferedReader keyboardBuffer = new BufferedReader(keyboardInput)) {
			System.out.println("Begin Chat:");
			String message = null;
			while (!(message = keyboardBuffer.readLine()).equals("EXIT")) {
				try (Socket sock = new Socket("localhost", 4230);
						PrintStream outgoingMessages = new PrintStream(sock.getOutputStream());
						InputStreamReader incomingMessages = new InputStreamReader(sock.getInputStream());
						BufferedReader incomingBuffer = new BufferedReader(incomingMessages)) {
					outgoingMessages.println(username + " : " + message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
