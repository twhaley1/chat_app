package com.whaley.chatserver.test.unittest.serversocket;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.serversocket.ServerEndpoint;
import com.whaley.chatserver.serversocket.ServerSocketEndpoint;
import com.whaley.chatserver.socket.ClientEndpoint;

public class TestServerSocket {

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
	
	private class TestServerSocketImpl extends ServerSocket {

		private boolean isClosed;
		private String input;
		
		public TestServerSocketImpl(String input) throws IOException {
			super();
			this.isClosed = false;
			this.input = input;
		}

		@Override
		public Socket accept() throws IOException {
			return new MockSocket(this.input);
		}

		@Override
		public void close() throws IOException {
			this.isClosed = true;
		}

		@Override
		public boolean isClosed() {
			return this.isClosed;
		}
		
	}
	
	@Test
	public void testNotAllowNullSocket() {
		assertThrows(IllegalArgumentException.class, () -> new ServerSocketEndpoint(null));
	}

	@Test
	public void testAcceptsCorrectly() throws IOException {
		ServerEndpoint endpoint = new ServerSocketEndpoint(new TestServerSocketImpl("testing"));
		
		ClientEndpoint sock = endpoint.accept();
		BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		String contents = reader.readLine();
		sock.close();
		endpoint.close();
		
		assertAll(() -> assertEquals("testing", contents),
				() -> assertEquals(true, endpoint.isClosed()));
	}
}
