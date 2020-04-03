package com.whaley.chatserver.service.outgoingmessages.systemstate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.outgoingmessages.IncomingMessageInterpreter;
import com.whaley.chatserver.service.outgoingmessages.IncomingMessageInterpreter.InterpretedData;
import com.whaley.chatserver.socket.ClientEndpoint;

public class SystemStateServer extends Server {

	private IncomingMessageInterpreter interpreter;
	private ClientStateUpdater updaterService;
	
	public SystemStateServer(ServerEndpoint serverConnection, ClientStateUpdater updaterService) {
		super(serverConnection, Runtime.getRuntime().availableProcessors());
		this.interpreter = new IncomingMessageInterpreter();
		this.updaterService = updaterService;
		this.startRunning(this.updaterService);
	}

	@Override
	protected void handleClient(ClientEndpoint client) throws IOException {
		String rawMessage = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
		if (this.interpreter.isValidFormat(rawMessage)) {
			InterpretedData extractedData = this.interpreter.extractData(rawMessage);
			this.performCommand(extractedData.getCommand().toLowerCase(), extractedData.getParameter().toLowerCase(), client);
		}
	}

	private void performCommand(String command, String parameter, ClientEndpoint client) throws IOException {
		switch (command) {
			case IncomingMessageInterpreter.ENTER_COMMAND:
				this.updaterService.addClient(parameter, new PrintStream(client.getOutputStream()));
				break;
			case IncomingMessageInterpreter.LEAVE_COMMAND:
				this.updaterService.removeClient(parameter);
				break;
			default:
				System.err.println("Invalid Command Syntax From " + client.getInetAddress());
		}
	}
	
	@Override
	public void closeServer() {
		this.updaterService.shutdown();
		super.closeServer();
	}
}
