package com.whaley.chatserver.test.unittest.service.incoming.incomingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.incoming.IncomingMessageInterpreter;
import com.whaley.chatserver.service.incoming.IncomingMessageInterpreter.InterpretedData;

public class TestInterpreter {

	@Test
	public void testAllFieldsEmpty() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "" + (char) 29 + "" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}

	@Test
	public void testAllFieldsEmptyExceptUsername() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "twhal" + (char) 29 + "" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testAllFieldsEmptyExceptUsernameAndContent() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "twhal" + (char) 29 + "How are you" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testAllFieldsEmptyExceptTimestamp() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "" + (char) 29 + "" + (char) 29 + "2342342";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testAllFieldsEmptyExceptTimestampAndContent() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "" + (char) 29 + "How are you" + (char) 29 + "2342342";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testOnlyContentNotEmpty() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "" + (char) 29 + "How are you" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testCorrectFormat() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "twhal" + (char) 29 + "How are you?" + (char) 29 + "2342342";
		
		assertEquals(true, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testCorrectFormatWithStripping() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "     twhal     " + (char) 29 + "    How are you?    " + (char) 29 + "   2342342   ";
		
		assertEquals(true, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testInvalidTimestamp() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "twhal" + (char) 29 + "How are you?" + (char) 29 + "234f3ssa!2342";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testCreatesCorrectMessage() {
		IncomingMessageInterpreter interpreter = new IncomingMessageInterpreter();
		String message = "     twhal   " + (char) 29 + "   How are you?   " + (char) 29 + "511616";
		
		InterpretedData data = interpreter.extractData(message);
		Message createdData = data.getMessage();
		
		assertAll(() -> assertEquals("twhal", createdData.getUsername()),
				() -> assertEquals("How are you?", createdData.getMessageContent()),
				() -> assertEquals(511616, createdData.getTimeSentMillis()));
	}
}
