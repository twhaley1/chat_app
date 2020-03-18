package com.whaley.chatserver.service.data;

public class Message {

	private String username;
	private String messageContent;
	private long timeSentMillis;
	
	public Message(String username, String messageContent, long timeSentMillis) {
		this.username = username;
		this.messageContent = messageContent;
		this.timeSentMillis = timeSentMillis;
	}
	
	public String getUsername() {
		return this.username;
	}
	public String getMessageContent() {
		return this.messageContent;
	}
	public long getTimeSentMillis() {
		return this.timeSentMillis;
	}
	
}
