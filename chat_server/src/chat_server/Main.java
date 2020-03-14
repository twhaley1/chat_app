package chat_server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import chat_server.service.IncomingMessageServer;
import chat_server.service.LoginServer;
import chat_server.service.OutgoingMessageServer;
import chat_server.service.Server;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Server login = new LoginServer(4225);
		BlockingQueue<String> buffer = new SynchronousQueue<String>();
		Server incoming = new IncomingMessageServer(4230, buffer);
		Server outgoing = new OutgoingMessageServer(4235, buffer);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			login.close();
			incoming.close();
			outgoing.close();
		}));
		
		Thread loginThread = new Thread(login);
		Thread incomingThread = new Thread(incoming);
		Thread outgoingThread = new Thread(outgoing);
		
		loginThread.start();
		incomingThread.start();
		outgoingThread.start();
		
		loginThread.join();
		incomingThread.join();
		outgoingThread.join();
	}
}
