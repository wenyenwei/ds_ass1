package ass1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class WordProcessor {
	Map<String, String> wordDatabase = new HashMap<String, String>();
	// save to dictionary
	public WordProcessor() {
		wordDatabase.put("apple", "A kind of fruit. Red and round.");
		wordDatabase.put("banana", "A kind of fruit. Yellow and long.");
		
	}
	// get from dictionary
	public String getWord(String word) {
		System.out.println(wordDatabase.get(word));
		System.out.println("Looking up definition for word " + word);
		return wordDatabase.get(word);
	}
}

class ServerThread extends Thread {
	// initialize dictionary object
	WordProcessor wordWorker = new WordProcessor();
	
	// initialize socket
	Socket socket;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			// in and out stream
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			System.out.println("Waiting for client input...");
			
			// buffer client input
			byte buffer[] = new byte[1024];
			in.read(buffer);
			
			// print out client input and server message
			String clinetInput = new String(buffer).trim();
			System.out.println("Received: " + clinetInput);
			
			// get word from dictionary
			String getFromDict = wordWorker.getWord(clinetInput);
			System.out.println("Definition retrieved: "+ getFromDict);
			if (getFromDict == null) {
				getFromDict = "This word doesn't exist in the dictionary.";
			}

			// send reply to client	
			out.write(getFromDict.getBytes());
			System.out.println("Response sent...");
			
			// close sockets
			socket.close();			
			
		}catch (Exception e) {
			System.out.println("Error: " + e);
		}
	
	}
}

public class Server {

	public static void main(String[] args) throws IOException {
//		// set up a datagram socket
//		DatagramSocket dgSocket = new DatagramSocket(8080);
//		
//		// create datagram packet to receive packet
//		DatagramPacket dgPacket = new DatagramPacket(new byte[1024], 1024);
//		
//		// receive packet
//		dgSocket.receive(dgPacket);
//		
//		// print out received data
//		System.out.println(new String(dgPacket.getData()));
//		System.out.println("Obtained from IP: " + dgPacket.getAddress());
//		System.out.println("Obtained from Port: " + dgPacket.getPort());
//		
//		// close the datagram socket
//		dgSocket.close();
		
		// set up server socket
		ServerSocket serverSocket = new ServerSocket(9090);
		System.out.println("Listening on port 9090...");
		
		// process one thread, end thread, open for new connection
		while(true) {
			// accept client request
			System.out.println("Waiting for client..");
			Socket socket = serverSocket.accept();
			System.out.println("Client connected");
			
			// create new thread for each client connection
			System.out.println("Starting a new server thread..");
			new ServerThread(socket).start(); 

		}
	}

}
