package com.whaley.chatserver;

import java.io.IOException;
import java.net.ServerSocket;

import com.whaley.chatserver.chat.Chat;
import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.serversocket.ServerSocketEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.incoming.IncomingMessageServer;
import com.whaley.chatserver.service.outgoing.OutgoingMessageServer;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		if (args.length != 3) {
			System.err.println("3 Arguments Expected. " + args.length + " Received.");
			System.exit(1);
		}

		try {
			int incomingMessagePort = Integer.parseInt(args[1]);
			int outgoingMessagePort = Integer.parseInt(args[2]);
			if (incomingMessagePort == outgoingMessagePort) {
				System.err.println("The Ports Given As Program Arguments Can Not Be The Same.");
				System.exit(2);
			}
			if (incomingMessagePort < 0 || outgoingMessagePort < 0) {
				System.err.println("The Ports Given As Program Arguments Must Be Greater Than Or Equal To Zero.");
				System.exit(3);
			}
			String serverTitle = args[0];

			SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
			Server incomingConnectionServer = new IncomingMessageServer(new ServerSocketEndpoint(new ServerSocket(incomingMessagePort)), buffer);
			Server outgoingConnectionServer = new OutgoingMessageServer(new ServerSocketEndpoint(new ServerSocket(outgoingMessagePort)), buffer);
			Chat server = new Chat(serverTitle, incomingConnectionServer, outgoingConnectionServer);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				server.end();
			}));

			System.out.println("Chat Server Started.");
			server.begin();
		} catch (NumberFormatException e) {
			System.err.println(
					"Invalid Program Arguments. Expects: [\"title\"] [IncomingMessagePort] [OutgoingMessagePort]");
			System.exit(4);
		} catch (IOException e) {
			System.err.println("Incoming/Outgoing Servers Could Not Be Started. Try Different Port Numbers.");
			System.exit(5);
		}
	}

}
