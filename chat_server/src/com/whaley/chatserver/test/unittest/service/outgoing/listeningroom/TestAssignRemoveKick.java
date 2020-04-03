package com.whaley.chatserver.test.unittest.service.outgoing.listeningroom;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.outgoingmessages.ListeningClients;

public class TestAssignRemoveKick {

	@Test
	public void testAssigns() {
		ListeningClients room = new ListeningClients();
		room.addClient("twhal", new PrintStream(new ByteArrayOutputStream()));
		
		assertEquals(true, room.getListeningClientUsernames().contains("twhal"));
	}

	@Test
	public void testRemoves() {
		ListeningClients room = new ListeningClients();
		room.addClient("twhal", new PrintStream(new ByteArrayOutputStream()));
		room.removeClient("twhal");
		
		assertEquals(false, room.getListeningClientUsernames().contains("twhal"));
	}
	
	@Test
	public void testKicks() {
		ListeningClients room = new ListeningClients();
		room.addClient("twhal", new PrintStream(new ByteArrayOutputStream()));
		room.addClient("rdav", new PrintStream(new ByteArrayOutputStream()));
		
		room.kickClients();
		
		assertEquals(0, room.getListeningClientUsernames().size());
	}
}
