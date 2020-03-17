package com.whaley.chatserver.service.outgoing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.whaley.chatserver.service.messageinterpreter.ClientMessageInterpreter;

public class OutgoingServerHandleInterpreter extends ClientMessageInterpreter {

	private static final int NUMBER_OF_INPUTS = 2;
	
	private Collection<String> commands;
	
	protected OutgoingServerHandleInterpreter() {
		super(" ");
		this.commands = new ArrayList<String>();
		this.commands.add("leave");
		this.commands.add("enter");
	}

	@Override
	public boolean isValidFormat(String message) {
		if (message == null) {
			return false;
		}
		
		String[] sections = this.split(message);
		if (sections.length == NUMBER_OF_INPUTS) {
			String command = sections[0];
			if (this.commands.contains(command.toLowerCase())) {
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
