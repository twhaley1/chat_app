package chat_server;

import java.io.IOException;

import chat_server.service.LoginServer;

public class Main {

	public static void main(String[] args) {
		LoginServer server = new LoginServer(4225);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> server.close()));
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			server.close();
		}
	}
}
