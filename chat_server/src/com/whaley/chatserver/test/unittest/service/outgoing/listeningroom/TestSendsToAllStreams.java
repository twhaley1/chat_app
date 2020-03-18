package com.whaley.chatserver.test.unittest.service.outgoing.listeningroom;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.data.Message;
import com.whaley.chatserver.service.outgoing.ListeningClients;

public class TestSendsToAllStreams {

	@Test
	public void testSendsToAllStreams() {
		ListeningClients room = new ListeningClients();
		OutputStream twhalOs = new ByteArrayOutputStream();
		OutputStream jbobOs = new ByteArrayOutputStream();
		room.addClient("twhal", new PrintStream(twhalOs));
		room.addClient("jbob", new PrintStream(jbobOs));
		
		room.sendToClients(new Message("twhal", "I love turkey", 1243213));
		
		assertAll(() -> assertEquals("twhal: I love turkey" + System.lineSeparator(), twhalOs.toString()),
				() -> assertEquals("twhal: I love turkey" + System.lineSeparator(), jbobOs.toString()));
	}

}
