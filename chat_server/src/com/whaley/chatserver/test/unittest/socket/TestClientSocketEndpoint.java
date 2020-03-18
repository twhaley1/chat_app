package com.whaley.chatserver.test.unittest.socket;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.socket.ClientEndpoint;
import com.whaley.chatserver.socket.ClientSocketEndpoint;

public class TestClientSocketEndpoint {

	private class TestSocketImpl extends SocketImpl {

		private InputStream is;
		private OutputStream os;
		
		public TestSocketImpl(String input) {
			this.is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
			this.os = new ByteArrayOutputStream();
		}
		
		@Override
		protected InetAddress getInetAddress() {
			try {
				return InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				return null;
			}
		}
		
		@Override
		public Object getOption(int arg0) throws SocketException {
			return null;
		}

		@Override
		public void setOption(int arg0, Object arg1) throws SocketException {
		}

		@Override
		protected void accept(SocketImpl arg0) throws IOException {
		}

		@Override
		protected int available() throws IOException {
			return 0;
		}

		@Override
		protected void bind(InetAddress arg0, int arg1) throws IOException {	
		}

		@Override
		protected void close() throws IOException {
		}

		@Override
		protected void connect(String arg0, int arg1) throws IOException {
		}

		@Override
		protected void connect(InetAddress arg0, int arg1) throws IOException {
		}

		@Override
		protected void connect(SocketAddress arg0, int arg1) throws IOException {
		}

		@Override
		protected void create(boolean arg0) throws IOException {
		}

		@Override
		protected InputStream getInputStream() throws IOException {
			return this.is;
		}

		@Override
		protected OutputStream getOutputStream() throws IOException {
			return this.os;
		}

		@Override
		protected void listen(int arg0) throws IOException {
		}

		@Override
		protected void sendUrgentData(int arg0) throws IOException {	
		}
		
	}
	
	private class MockSocket extends Socket {
		
		public MockSocket(String input) throws IOException {
			super(new TestSocketImpl(input));
		}

		@Override
		public boolean isConnected() {
			return true;
		}
		
	}
	
	@Test
	public void testWorksCorrectly() throws IOException {
		ClientEndpoint endpoint = new ClientSocketEndpoint(new MockSocket("testing"));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(endpoint.getInputStream()));
		String isContent = reader.readLine();
		String osContent = endpoint.getOutputStream().toString();
		String address = endpoint.getInetAddress();
		reader.close();
		assertEquals("testing", isContent);
		assertEquals("", osContent);
		assertEquals("localhost/127.0.0.1", address);
	}

	@Test
	public void testClosesCorrectly() throws IOException {
		Socket sock = new MockSocket("testing");
		ClientEndpoint endpoint = new ClientSocketEndpoint(sock);
		endpoint.close();
		
		assertEquals(true, sock.isClosed());
	}
	
	@Test
	public void testNotAllowNullSocket() {
		assertThrows(IllegalArgumentException.class, () -> new ClientSocketEndpoint(null));
	}
}
