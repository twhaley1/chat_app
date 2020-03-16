package chat_server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Streamable {

	InputStream getInputStream() throws IOException;
	
	OutputStream getOutputStream() throws IOException;
	
	void close() throws IOException;
	
	String getInetAddress();
	
}
