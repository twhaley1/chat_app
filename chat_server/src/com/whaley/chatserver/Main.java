package com.whaley.chatserver;

import java.io.IOException;
import java.net.ServerSocket;

import com.whaley.chatserver.chat.Chat;
import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.serversocket.ServerSocketEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.incoming.IncomingMessageServer;
import com.whaley.chatserver.service.outgoing.OutgoingMessageServer;

public class Main {

	private static final int EXPECTED_NUMBER_OF_ARGS = 3;
	
	public static void main(String[] args) throws InterruptedException {
		if (args.length != EXPECTED_NUMBER_OF_ARGS) {
			System.err.println(EXPECTED_NUMBER_OF_ARGS + " Arguments Expected. " + args.length + " Received.");
			System.exit(1);
		}

		int[] ports = fetchPorts(args[1], args[2]);
		Server[] servers = fetchServers(ports[0], ports[1]);
		String serverTitle = args[0];

		Chat server = new Chat(serverTitle, servers[0], servers[1]);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.endChat();
		}));
		System.out.println("Chat Server Started.");
		server.startChat();
	}

	private static Server[] fetchServers(int incomingMessageServerPort, int outgoingMessageServerPort) {
		Server[] servers = new Server[2];
		try {
			SynchronizedQueue<Message> incomingOutgoingExchangeBuffer = new SynchronizedQueue<Message>();
			
			ServerEndpoint incomingServerConnection = new ServerSocketEndpoint(new ServerSocket(incomingMessageServerPort));
			Server incomingConnectionServer = new IncomingMessageServer(incomingServerConnection, incomingOutgoingExchangeBuffer);
			
			ServerEndpoint outgoingServerConnection = new ServerSocketEndpoint(new ServerSocket(outgoingMessageServerPort));
			Server outgoingConnectionServer = new OutgoingMessageServer(outgoingServerConnection, incomingOutgoingExchangeBuffer);
			
			servers[0] = incomingConnectionServer;
			servers[1] = outgoingConnectionServer;
		} catch (IOException e) {
			System.err.println("Incoming/Outgoing Servers Could Not Be Started. Try Different Port Numbers.");
			System.exit(5);
		}
		return servers;
	}
	
	private static int[] fetchPorts(String firstPort, String secondPort) {
		int[] ports = new int[2];
		try {
			int incomingMessagePort = Integer.parseInt(firstPort);
			int outgoingMessagePort = Integer.parseInt(secondPort);
			if (incomingMessagePort == outgoingMessagePort) {
				System.err.println("The Ports Given As Program Arguments Can Not Be The Same.");
				System.exit(2);
			}
			if (incomingMessagePort < 0 || outgoingMessagePort < 0) {
				System.err.println("The Ports Given As Program Arguments Must Be Greater Than Or Equal To Zero.");
				System.exit(3);
			}
			
			ports[0] = incomingMessagePort;
			ports[1] = outgoingMessagePort;
		} catch (NumberFormatException e) {
			System.err.println(
					"Invalid Program Arguments. Expects: [\"title\"] [IncomingMessageServerPort] [OutgoingMessageServerPort]");
			System.exit(4);
		}
		return ports;
	}
}
