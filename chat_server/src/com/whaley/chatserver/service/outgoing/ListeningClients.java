package com.whaley.chatserver.service.outgoing;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.whaley.chatserver.service.data.Message;

public class ListeningClients {

	private Map<String, PrintStream> room;
	
	public ListeningClients() {
		this.room = new HashMap<String, PrintStream>();
	}
	
	public void addClient(String clientUsername, PrintStream clientListeningStream) {
		synchronized (this) {
			this.room.put(clientUsername, clientListeningStream);
		}
	}
	
	public void removeClient(String clientUsername) {
		synchronized (this) {
			this.room.remove(clientUsername);
		}
	}
	
	public void kickClients() {
		synchronized (this) {
			for (PrintStream client : this.room.values()) {
				client.close();
			}
			this.room.clear();
		}
	}
	
	public Collection<String> getListeningClientUsernames() {
		synchronized (this) {
			return Collections.unmodifiableCollection(this.room.keySet());
		}
	}
	
	public void sendToClients(Message message) {
		synchronized (this) {
			for (PrintStream client : this.room.values()) {
				client.println(message.getUsername() + ": " + message.getMessageContent());
			}
		}
	}
}
