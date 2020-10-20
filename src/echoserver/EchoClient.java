package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		Socket socket = new Socket("localhost", PORT_NUMBER);
		InputStream socketInputStream = socket.getInputStream();
		OutputStream socketOutputStream = socket.getOutputStream();

		toServer toServer = new toServer(socketOutputStream, socket);
		Thread outThread = new Thread(toServer);
		fromServer fromServer = new fromServer(socketInputStream, socket);
		Thread inThread = new Thread(fromServer);

		outThread.start();
		inThread.start();
	}

	public class fromServer implements Runnable {
		InputStream in;
		Socket sock;

		public fromServer(InputStream in, Socket sock) {
			this.in = in;
			this.sock = sock;
		}
		public void run() {
			try {
				int n;
				while ((n = in.read()) != -1) {
					System.out.write(n);
					System.out.flush();
				}
				sock.shutdownInput();
			}
			catch (IOException ioe) {
				System.out.println("We caught an unexpected exception");
			}
		}
	}

	public class toServer implements Runnable {
		OutputStream out;
		Socket socket;

		public toServer(OutputStream out, Socket sock) throws IOException {
			this.out = out;
			this.socket = sock;
		}

		public void run() {
			try {
				int n;
				while ((n = System.in.read()) != -1) {
					out.write(n);
				}
				out.flush();
				socket.shutdownOutput();
			}
			catch (IOException ioe) {
				System.out.println("We caught an unexpected exception");
			}
		}
	}
}