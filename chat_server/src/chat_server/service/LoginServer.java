package chat_server.service;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import chat_server.users.RegisteredUsers;
import chat_server.users.User;

public class LoginServer extends Server {

	private RegisteredUsers register;
	
	public LoginServer(int port) {
		super(port);
		
		List<User> users = new ArrayList<User>();
		users.add(new User("twhal", "password"));
		this.register = new RegisteredUsers(users);
	}

	@Override
	protected void handle(Socket client) {
		this.execute(new LoginService(client, this.register));
	}
	
}
