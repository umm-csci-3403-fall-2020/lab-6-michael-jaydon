package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class EchoServer {
	
	// REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
	private static final int PORT_NUMBER = 6013;
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		ExecutorService executor = Executors.newCachedThreadPool();
		while (true) {
			Socket socket = serverSocket.accept();
			InputStream socketInputStream = socket.getInputStream();
			OutputStream socketOutputStream = socket.getOutputStream();
			executor.submit(new readClient(socket, socketOutputStream, socketInputStream));
			executor.shutdown();
//			readClient client = new readClient(socket, socketOutputStream, socketInputStream);
//			Thread thread = new Thread(client);
//			thread.start();

			// Put your code here.
			// This should do very little, essentially:
			//   * Construct an instance of your runnable class
			//   * Construct a Thread with your runnable
			//      * Or use a thread pool
			//   * Start that thread
		}
	}

	public static class readClient implements Runnable {
		Socket socket;
		OutputStream out;
		InputStream in;
		readClient(Socket socket, OutputStream out, InputStream in) {
			this.socket = socket;
			this.out = out;
			this.in = in;
		}
		public void run() {
			try {
				int n;
				while ((n = in.read()) != -1) {
					out.write(n);
				}
				socket.shutdownOutput();
				socket.shutdownInput();
			} catch(IOException ioe) {
				System.out.println("We caught an unexpected exception");
			}
		}
	}
}