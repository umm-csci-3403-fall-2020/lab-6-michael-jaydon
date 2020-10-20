package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	// REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
	public static final int PORT_NUMBER = 6013;
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		while (true) {
			Socket socket = serverSocket.accept();
			InputStream socketInputStream = socket.getInputStream();
			OutputStream socketOutputStream = socket.getOutputStream();
			readClient client = new readClient(socketOutputStream, socketInputStream);
			Thread thread = new Thread(client);
			thread.start();

			// Put your code here.
			// This should do very little, essentially:
			//   * Construct an instance of your runnable class
			//   * Construct a Thread with your runnable
			//      * Or use a thread pool
			//   * Start that thread
		}
	}

	public class readClient implements Runnable {
		OutputStream out;
		InputStream in;
		public readClient(OutputStream out, InputStream in) {
			this.out = out;
			this.in = in;
		}
		public void run() {
			try {
				int n;
				while ((n = in.read()) != -1) {
					out.write(n);
				}
			} catch(IOException ioe) {
				System.out.println("We caught an unexpected exception");
			}
		}
	}
}