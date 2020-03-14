package chat_server.users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegisteredUsers {

	private Set<User> users;
	
	public RegisteredUsers(Collection<? extends User> loadedUsers) {
		this.users = new HashSet<User>(loadedUsers);
	}
	
	public synchronized boolean login(String username, String password) {
		boolean isFound = false;
		for (User user : this.users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				isFound = true;
			}
		}
		return isFound;
	}
}
