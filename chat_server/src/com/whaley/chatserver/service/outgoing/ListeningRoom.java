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
	
	public void assignListener(String username, PrintStream stream) {
		synchronized (this) {
			this.room.put(username, stream);
		}
	}
	
	public void removeListener(String username) {
		synchronized (this) {
			this.room.remove(username);
		}
	}
	
	public void kickListeners() {
		synchronized (this) {
			for (PrintStream client : this.room.values()) {
				client.close();
			}
			this.room.clear();
		}
	}
	
	public Collection<String> getListeningUsernames() {
		synchronized (this) {
			return Collections.unmodifiableCollection(this.room.keySet());
		}
	}
	
	public void sendToListeners(Message message) {
		synchronized (this) {
			for (PrintStream client : this.room.values()) {
				client.println(message.getUsername() + ": " + message.getContent());
			}
		}
	}
}
