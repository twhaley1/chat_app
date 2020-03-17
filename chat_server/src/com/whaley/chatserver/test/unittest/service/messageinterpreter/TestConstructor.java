package com.whaley.chatserver.test.unittest.service.messageinterpreter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.messageinterpreter.ClientMessageInterpreter;

public class TestConstructor {

	private class TestInterpreter extends ClientMessageInterpreter {

		public TestInterpreter(String delimeter) {
			super(delimeter);
		}

		@Override
		public boolean isValidFormat(String message) {
			return false;
		}

		@Override
		public Object extractData(String message) {
			return null;
		}
		
	}
	
	@Test
	public void testNotAllowNullDelimeter() {
		assertThrows(IllegalArgumentException.class, () -> new TestInterpreter(null));
	}

}
