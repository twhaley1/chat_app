package chat_server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import chat_server.chat.Chat;
import chat_server.serversocket.ServerSocketEndpoint;
import chat_server.service.Server;
import chat_server.service.incoming.IncomingMessageServer;
import chat_server.service.outgoing.OutgoingMessageServer;
import chat_server.service.outgoing.TimeoutService;
import chat_server.data.Message;

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

			Queue<Message> buffer = new LinkedList<Message>();
			Server incoming = new IncomingMessageServer(new ServerSocketEndpoint(incomingMessagePort), buffer);
			Server outgoing = new OutgoingMessageServer(new ServerSocketEndpoint(outgoingMessagePort), buffer, TimeoutService.TIMEOUT_ONE_MINUTE);
			Chat server = new Chat(serverTitle, incoming, outgoing);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				server.end();
			}));
			
			System.out.println("Chat Server Started.");
			server.begin();
		} catch (NumberFormatException e) {
			System.err.println("Invalid Program Arguments. Expects: [\"title\"] [IncomingMessagePort] [OutgoingMessagePort]");
			System.exit(4);
		} catch (IOException e) {
			System.err.println("Incoming/Outgoing Servers Could Not Be Started. Try Different Port Numbers.");
			System.exit(5);
		}
	}
	
}
