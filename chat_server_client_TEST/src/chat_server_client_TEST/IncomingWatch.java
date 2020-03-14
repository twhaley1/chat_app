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
			String result = incomingBuffer.readLine();
			while (!result.equals("83530398293485893853495830")) {
				System.out.println(result);
				result = incomingBuffer.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
