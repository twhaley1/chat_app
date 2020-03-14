package chat_server_client_TEST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class IncomingWatch implements Runnable {
	
	@Override
	public void run() {
		try (Socket sock = new Socket("localhost", 4235);
				PrintStream outgoingMessages = new PrintStream(sock.getOutputStream());
				InputStreamReader incomingMessages = new InputStreamReader(sock.getInputStream());
				BufferedReader incomingBuffer = new BufferedReader(incomingMessages)) {
			
			outgoingMessages.println("listening");
			String result = null;
			while (!(result = incomingBuffer.readLine()).equals("5449815165132468")) {
				System.out.println(result);
				result.toString();
			}
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}

}
