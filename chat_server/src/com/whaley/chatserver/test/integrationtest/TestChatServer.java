package com.whaley.chatserver.test.integrationtest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.chat.Chat;
import com.whaley.chatserver.data.Message;
import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.service.Server;
import com.whaley.chatserver.service.bridge.SynchronizedQueue;
import com.whaley.chatserver.service.incoming.IncomingMessageServer;
import com.whaley.chatserver.service.outgoing.OutgoingMessageServer;
import com.whaley.chatserver.socket.ClientEndpoint;


public class TestChatServer {
	
	private class TestIncomingMessageServerEndpoint implements ServerEndpoint {

		private Queue<String> messages;
		
		public TestIncomingMessageServerEndpoint() {
			this.messages = new LinkedList<String>();
			this.messages.add("  twhal  " + ((char) 29) + " What's up mate? " + ((char) 29) + " 1");
			this.messages.add("   jbob" + ((char) 29) + " I'm doing well" + ((char) 29) + " 2343242");
			this.messages.add(" myikes     : Hello, there fella :");
			this.messages.add("ff");
		}
		
		@Override
		public ClientEndpoint accept() throws IOException {
			String message = this.messages.remove();
			return new TestClientEndpoint(message);
		}

		@Override
		public void close() throws IOException {
			this.messages.clear();
		}

		@Override
		public boolean isClosed() {
			return this.messages.isEmpty();
		}
		
	}
	
	private class TestClientEndpoint implements ClientEndpoint {

		private InputStream is;
		private OutputStream os;
		
		public TestClientEndpoint(String message) {
			this.is = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
			this.os = new ByteArrayOutputStream();
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			return this.is;
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return this.os;
		}

		@Override
		public void close() throws IOException {
			this.is.close();
			this.os.close();
		}

		@Override
		public String getInetAddress() {
			return "127.0.0.1";
		}
		
	}
	
	private class TestOutgoingMessageServerEndpoint implements ServerEndpoint {

		private Queue<String> users;
		private List<OutputStream> outputs;
		
		public TestOutgoingMessageServerEndpoint() {
			this.users = new LinkedList<String>();
			this.users.add("  eNter  twhal ");
			this.users.add(" ENTER   jbob");
			this.users.add("enteR myikes  ");
			
			this.outputs = new ArrayList<OutputStream>();
		}
		
		@Override
		public ClientEndpoint accept() throws IOException {
			String username = this.users.remove();
			ClientEndpoint endpoint = new TestClientEndpoint(username);
			this.outputs.add(endpoint.getOutputStream());
			return endpoint;
		}

		@Override
		public void close() throws IOException {
			this.users.clear();
		}

		@Override
		public boolean isClosed() {
			return this.users.isEmpty();
		}
		
		public List<OutputStream> getOutputs() {
			return this.outputs;
		}
	}
	
	@Test
	public void testMessageExchange() throws InterruptedException {
		SynchronizedQueue<Message> buffer = new SynchronizedQueue<Message>();
		Server incoming = new IncomingMessageServer(new TestIncomingMessageServerEndpoint(), buffer);
		
		TestOutgoingMessageServerEndpoint outgoingEndpoint = new TestOutgoingMessageServerEndpoint();
		Server outgoing = new OutgoingMessageServer(outgoingEndpoint, buffer);
		
		Chat chat = new Chat("Integration Test", incoming, outgoing);
		chat.begin();
		chat.end();
		
		List<String> sentToClients = new ArrayList<String>();
		for (OutputStream stream : outgoingEndpoint.getOutputs()) {
			String output = stream.toString();
			sentToClients.add(output);
		}
		
		assertAll(() -> assertEquals(3, sentToClients.size()),
				() -> assertEquals(true, sentToClients.get(0).contains("twhal: What's up mate?" + System.lineSeparator())),
				() -> assertEquals(true, sentToClients.get(0).contains("jbob: I'm doing well" + System.lineSeparator())),
				() -> assertEquals(true, sentToClients.get(1).contains("twhal: What's up mate?" + System.lineSeparator())),
				() -> assertEquals(true, sentToClients.get(1).contains("jbob: I'm doing well" + System.lineSeparator())),
				() -> assertEquals(true, sentToClients.get(2).contains("twhal: What's up mate?" + System.lineSeparator())),
				() -> assertEquals(true, sentToClients.get(2).contains("jbob: I'm doing well" + System.lineSeparator())));
	}

}
