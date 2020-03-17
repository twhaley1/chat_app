package com.whaley.chatserver.test.unittest.service.incoming.incomingmessageserver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.incoming.IncomingServerHandleInterpreter;
import com.whaley.chatserver.service.incoming.IncomingServerHandleInterpreter.InterpretedData;

public class TestInterpreter {

	@Test
	public void testAllFieldsEmpty() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "" + (char) 29 + "" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}

	@Test
	public void testAllFieldsEmptyExceptUsername() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "twhal" + (char) 29 + "" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testAllFieldsEmptyExceptUsernameAndContent() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "twhal" + (char) 29 + "How are you" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testAllFieldsEmptyExceptTimestamp() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "" + (char) 29 + "" + (char) 29 + "2342342";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testAllFieldsEmptyExceptTimestampAndContent() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "" + (char) 29 + "How are you" + (char) 29 + "2342342";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testOnlyContentNotEmpty() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "" + (char) 29 + "How are you" + (char) 29 + "";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testCorrectFormat() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "twhal" + (char) 29 + "How are you?" + (char) 29 + "2342342";
		
		assertEquals(true, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testCorrectFormatWithStripping() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "     twhal     " + (char) 29 + "    How are you?    " + (char) 29 + "   2342342   ";
		
		assertEquals(true, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testInvalidTimestamp() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "twhal" + (char) 29 + "How are you?" + (char) 29 + "234f3ssa!2342";
		
		assertEquals(false, interpreter.isValidFormat(message));
	}
	
	@Test
	public void testCreatesCorrectMessage() {
		IncomingServerHandleInterpreter interpreter = new IncomingServerHandleInterpreter();
		String message = "     twhal   " + (char) 29 + "   How are you?   " + (char) 29 + "511616";
		
		InterpretedData data = interpreter.extractData(message);
		Message createdData = data.getMessage();
		
		assertAll(() -> assertEquals("twhal", createdData.getUsername()),
				() -> assertEquals("How are you?", createdData.getContent()),
				() -> assertEquals(511616, createdData.getTimestamp()));
	}
}
