import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoClient {

    private Socket socket;

    public EchoClient(String host, int port) throws IOException {
	socket = new Socket(host, port);
    }

    private void run() throws IOException {
	// receive server data
	Thread reader = new Thread(new Runnable() {
	    @Override
	    public void run() {
		try {
		    InputStream in = socket.getInputStream();
		    byte[] buffer = new byte[1024];
		    int n;
	 	    while((n = in.read(buffer)) > 0) {
			System.out.print("Client receive:");
		        System.out.write(buffer, 0, n);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	});
	reader.start();

	// read input from user and send to server
	OutputStream out = socket.getOutputStream();
	byte[] buffer = new byte[1024];
	int n;
	while((n = System.in.read(buffer)) > 0) {
	    out.write(buffer, 0, n);
	}		
    }

    public static void main(String[] args) throws IOException {
	EchoClient client = new EchoClient("localhost", 9988);
	client.run();
    }



}
