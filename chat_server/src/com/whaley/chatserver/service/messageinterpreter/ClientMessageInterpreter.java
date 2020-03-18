package com.whaley.chatserver.service.messageinterpreter;

public abstract class ClientMessageInterpreter {

	private String messageDelimeter;
	
	protected ClientMessageInterpreter(String messageDelimeter) {
		if (messageDelimeter == null) {
			throw new IllegalArgumentException("delimeter should not be null");
		}
		
		this.messageDelimeter = messageDelimeter;
	}
	
	public abstract boolean isValidFormat(String rawMessage);
	
	public abstract Object extractData(String rawMessage);
	
	protected String[] split(String rawMessage) {
		return rawMessage.strip().split(this.messageDelimeter);
	}
}
