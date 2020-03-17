package com.whaley.chatserver.service.messageinterpreter;

public abstract class ClientMessageInterpreter {

	private String delimeter;
	
	protected ClientMessageInterpreter(String delimeter) {
		if (delimeter == null) {
			throw new IllegalArgumentException("delimeter should not be null");
		}
		
		this.delimeter = delimeter;
	}
	
	public abstract boolean isValidFormat(String message);
	
	public abstract Object extractData(String message);
	
	protected String[] split(String message) {
		return message.strip().split(this.delimeter);
	}
}
