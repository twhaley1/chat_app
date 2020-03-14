package chat_server.users;

public class User {

	private String username;
	private String password;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			return this.username.equals(other.getUsername());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.username.hashCode() + this.password.hashCode();
	}

	@Override
	public String toString() {
		return "Username: " + this.username + " Password: " + this.password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
	
}
