package com.whaley.chatserver.service.outgoingmessages.systemstate;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.whaley.chatserver.service.outgoingmessages.ListeningClients;

public class ClientStateUpdater implements Runnable {

	private Map<String, PrintStream> connections;
	private ListeningClients chatRoom;
	
	private volatile boolean isRunning;
	private volatile boolean isStateChanged;
	
	public ClientStateUpdater(ListeningClients chatRoom) {
		this.chatRoom = chatRoom;
		this.connections = Collections.synchronizedMap(new HashMap<String, PrintStream>());
		this.isRunning = false;
		this.isStateChanged = false;
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		while (this.isRunning) {
			if (this.isStateChanged) {
				synchronized (this) {
					Collection<String> usernames = null;
					synchronized (this.chatRoom) {
						usernames = this.chatRoom.getListeningClientUsernames();
					}
					StringJoiner joiner = new StringJoiner(":");
					usernames.forEach(name -> joiner.add(name));
					String delimetedUsers = joiner.toString();
					synchronized (this.connections) {
						this.connections.values().forEach(connection -> connection.print(delimetedUsers));
					}
					this.isStateChanged = false;
				}
			}
		}
	}

	public void markStateChanged() {
		this.isStateChanged = true;
	}
	
	public void shutdown() {
		this.isRunning = false;
		this.isStateChanged = false;
	}
	
	public void addClient(String alias, PrintStream connection) {
		synchronized (this.connections) {
			this.connections.put(alias, connection);
			this.isStateChanged = true;
			System.out.println(alias + ": is logged in to the system");
		}
	}
	
	public void removeClient(String alias) {
		synchronized (this.connections) {
			PrintStream connection = this.connections.remove(alias);
			connection.close();
			this.isStateChanged = true;
			System.out.println(alias + ": is no longer logged in");
		}
	}
}
