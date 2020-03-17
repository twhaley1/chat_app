package com.whaley.chatserver.test.unittest.service.outgoing.listeningroom;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.outgoing.ListeningRoom;

public class TestAssignRemoveKick {

	@Test
	public void testAssigns() {
		ListeningRoom room = new ListeningRoom();
		room.assignListener("twhal", new PrintStream(new ByteArrayOutputStream()));
		
		assertEquals(true, room.getListeningUsernames().contains("twhal"));
	}

	@Test
	public void testRemoves() {
		ListeningRoom room = new ListeningRoom();
		room.assignListener("twhal", new PrintStream(new ByteArrayOutputStream()));
		room.removeListener("twhal");
		
		assertEquals(false, room.getListeningUsernames().contains("twhal"));
	}
	
	@Test
	public void testKicks() {
		ListeningRoom room = new ListeningRoom();
		room.assignListener("twhal", new PrintStream(new ByteArrayOutputStream()));
		room.assignListener("rdav", new PrintStream(new ByteArrayOutputStream()));
		
		room.kickListeners();
		
		assertEquals(0, room.getListeningUsernames().size());
	}
}
