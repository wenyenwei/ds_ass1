package ass1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
//		// set up a datagram socket
//		DatagramSocket dgSocket = new DatagramSocket();
//		
//		// create msg to send
//		String msg = "Hello from UDP client side";
//		byte[] data = msg.getBytes();
//		
//		// create packet
//		DatagramPacket dgPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8080);
//		
//		// send packet
//		dgSocket.send(dgPacket);
//		
//		// close UDP socket
//		dgSocket.close();
		
		// connect to server socket
		Socket socket = new Socket("127.0.0.1", 9090);
		System.out.println("Connected to server...");
		
		// input of client
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
				
		String wordToGetFromDict = "";
		//prevent null input
		while (wordToGetFromDict.length() == 0) {
			// write client message
			System.out.println("Enter word to retreive from the dictionary:");
			Scanner keyboard = new Scanner(System.in);
			wordToGetFromDict = keyboard.nextLine();	
		}
		
		System.out.println("Sending word request..");
		out.write(wordToGetFromDict.getBytes());
		
		// get server message
		byte[] response = new byte[1024];
		in.read(response);
		
		// print out server message
		System.out.println("The definition of "+wordToGetFromDict+" is:");
		System.out.println(new String(response));
		// close sockets
		socket.close();
	}

}
