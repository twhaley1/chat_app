package com.whaley.chatserver.data;

public class Message {

	private String username;
	private String content;
	private long timestamp;
	
	public Message(String username, String content, long timestamp) {
		this.username = username;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public String getUsername() {
		return this.username;
	}
	public String getContent() {
		return this.content;
	}
	public long getTimestamp() {
		return this.timestamp;
	}
	
}
