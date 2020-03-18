package com.whaley.chatserver.service.outgoing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.whaley.chatserver.service.messageinterpreter.ClientMessageInterpreter;

public class IncomingMessageInterpreter extends ClientMessageInterpreter {

	public static final String LEAVE_COMMAND = "leave";
	public static final String ENTER_COMMAND = "enter";
	
	private static final int EXPECTED_PIECES_OF_DATA = 2;
	private static final String MESSAGE_DELIMETER = " ";
	
	private Collection<String> validCommands;
	
	protected IncomingMessageInterpreter() {
		super(MESSAGE_DELIMETER);
		this.validCommands = new ArrayList<String>();
		this.validCommands.add(LEAVE_COMMAND);
		this.validCommands.add(ENTER_COMMAND);
	}

	@Override
	public boolean isValidFormat(String message) {
		if (message == null) {
			return false;
		}
		
		String[] sections = this.split(message);
		if (sections.length == EXPECTED_PIECES_OF_DATA) {
			String command = sections[0];
			if (this.validCommands.contains(command.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public InterpretedData extractData(String message) {
		String[] sections = this.split(message);
		return new InterpretedData(sections[0].toLowerCase(), sections[1]);
	}

	@Override
	protected String[] split(String message) {
		Collection<String> items = new ArrayList<String>(Arrays.asList(super.split(message)));
		items.removeIf(item -> item.isEmpty());
		return items.toArray(new String[items.size()]);
	}

	public class InterpretedData {
		
		private String command;
		private String parameter;
		
		private InterpretedData(String command, String parameter) {
			this.command = command;
			this.parameter = parameter;
		}
		
		public String getCommand() {
			return this.command;
		}
		
		public String getParameter() {
			return this.parameter;
		}
	}
}
