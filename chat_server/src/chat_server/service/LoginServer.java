package chat_server.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chat_server.users.RegisteredUsers;
import chat_server.users.User;

public class LoginServer {

	private RegisteredUsers register;
	
	private ExecutorService pool;
	private ServerSocket server;
	private int port;
	
	public LoginServer(int port) {
		this.port = port;
		this.pool = Executors.newFixedThreadPool(6);
		
		List<User> users = new ArrayList<User>();
		users.add(new User("twhal", "password"));
		this.register = new RegisteredUsers(users);
	}
	
	public void start() throws IOException {
		this.server = new ServerSocket(this.port);
		while (!this.server.isClosed()) {
			Socket client = this.server.accept();
			this.pool.execute(new LoginService(client, this.register));
		}
	}
	
	public void close() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred when trying to close the server.");
		}
	}
}
