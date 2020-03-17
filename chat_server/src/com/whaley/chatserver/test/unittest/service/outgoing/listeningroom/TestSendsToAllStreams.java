package com.whaley.chatserver.test.unittest.service.outgoing.listeningroom;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.service.outgoing.ListeningRoom;

public class TestSendsToAllStreams {

	@Test
	public void testSendsToAllStreams() {
		ListeningRoom room = new ListeningRoom();
		OutputStream twhalOs = new ByteArrayOutputStream();
		OutputStream jbobOs = new ByteArrayOutputStream();
		room.assignListener("twhal", new PrintStream(twhalOs));
		room.assignListener("jbob", new PrintStream(jbobOs));
		
		room.sendToListeners(new Message("twhal", "I love turkey", 1243213));
		
		assertAll(() -> assertEquals("twhal: I love turkey" + System.lineSeparator(), twhalOs.toString()),
				() -> assertEquals("twhal: I love turkey" + System.lineSeparator(), jbobOs.toString()));
	}

}
