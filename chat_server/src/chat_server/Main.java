package chat_server;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Chat anonymousChatter = new Chat("Welcome to Anonymous", 4230, 4235);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			anonymousChatter.cleanup();
		}));
		anonymousChatter.begin();
	}
	
}
