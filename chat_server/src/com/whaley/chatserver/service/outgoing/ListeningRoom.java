package com.whaley.chatserver.service.outgoing;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.whaley.chatserver.data.Message;

public class ListeningRoom {

	private Map<String, PrintStream> room;
	
	public ListeningRoom() {
		this.room = new HashMap<String, PrintStream>();
	}
	
	public synchronized void assignListener(String username, PrintStream stream) {
		this.room.put(username, stream);
	}
	
	public synchronized void removeListener(String username) {
		this.room.remove(username);
	}
	
	public synchronized void kickListeners() {
		for (PrintStream client : this.room.values()) {
			client.close();
		}
	}
	
	public synchronized Collection<String> getListeningUsernames() {
		return Collections.unmodifiableCollection(this.room.keySet());
	}
	
	public synchronized void sendToListeners(Message message) {
		for (PrintStream client : this.room.values()) {
			client.println(message.getUsername() + ": " + message.getContent());
		}
	}
}
