package com.whaley.chatserver.service.incoming;

import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.messageinterpreter.ClientMessageInterpreter;

public class IncomingMessageInterpreter extends ClientMessageInterpreter {

	private static final Character MESSAGE_DELIMETER = (char) 29;
	private static final int EXPECTED_PIECES_OF_DATA = 3;
	
	public IncomingMessageInterpreter() {
		super(MESSAGE_DELIMETER.toString());
	}

	@Override
	public boolean isValidFormat(String message) {
		if (message == null) {
			return false;
		}
		String[] sections = this.split(message);
		if (sections.length != EXPECTED_PIECES_OF_DATA) {
			return false;
		}
		String username = sections[0].strip();
		String content = sections[1].strip();
		String timestamp = sections[2].strip();
		if (username.isEmpty() || content.isEmpty() || timestamp.isEmpty()) {
			return false;
		}
		try {
			Long.parseLong(timestamp);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public InterpretedData extractData(String message) {
		String[] sections = this.split(message);
		return new InterpretedData(sections[0].strip(), sections[1].strip(), sections[2].strip());
	}

	public class InterpretedData {
		
		private String username;
		private String content;
		private long timestamp;
		
		private InterpretedData(String username, String content, String timestamp) {
			this.username = username;
			this.content = content;
			this.timestamp = Long.parseLong(timestamp);
		}
		
		public Message getMessage() {
			return new Message(this.username, this.content, this.timestamp);
		}
	}
}
