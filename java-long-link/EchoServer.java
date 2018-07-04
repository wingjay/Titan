import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class EchoServer {

    private ServerSocket server;

    public EchoServer(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void run() throws IOException {
	Socket client = server.accept();
	InputStream in = client.getInputStream();
	OutputStream out = client.getOutputStream();
	byte[] buffer = new byte[1024];
	int n;
	while((n = in.read(buffer)) > 0) {
	    System.out.println("Server echo: " + buffer);
	    out.write(buffer, 0, n);
	}	
    }

    public static void main(String[] args) throws IOException {
        try {
            EchoServer server = new EchoServer(9988);
 	    server.run();
	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}
    }

}
