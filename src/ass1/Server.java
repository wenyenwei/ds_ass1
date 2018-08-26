package ass1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

class WordProcessor {
	Map<String, String> wordDatabase = new HashMap<String, String>();
	// save to dictionary // add synchronization
	public WordProcessor() {
		wordDatabase.put("apple", "A kind of fruit. Red and round.");
		wordDatabase.put("banana", "A kind of fruit. Yellow and long.");
	}
	// get from dictionary
	public synchronized String getWord(String word) {
		System.out.println(wordDatabase.get(word));
		System.out.println("Looking up definition for word " + word);
		return wordDatabase.get(word);
	}
	// delete from dictionary
    public synchronized String deleteWord(String word) {
	    if (getWord(word) == null){
	        return "Word Not Found";
        }
        else{
            wordDatabase.remove(word);
            return "Success";
        }
    }
    // add to dictionary
    public synchronized String addMethod(String word, String definition){
        if (getWord(word) != null) {
            return "Word Already Exists.";
        }
        else {
            wordDatabase.put(word, definition);
            return "Success.";
        }
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

	private String getMethod(String target) {
        // print out client input and server message
        String clinetInput = target.trim();
        System.out.println("Received: " + clinetInput);

        // get word from dictionary
        String getFromDict = wordWorker.getWord(clinetInput);
        System.out.println("Definition retrieved: "+ getFromDict);
        if (getFromDict == null) {
            getFromDict = "Error: This word doesn't exist in the dictionary.";
        }
        return getFromDict;
    }

    private String deleteMethod(String target){
	    // print out client message
        String clinetInput = target.trim();
        System.out.println("Received: " + clinetInput);
        String resultStatus = "";
        // delete word from dictionary
        try {
            resultStatus = wordWorker.deleteWord(clinetInput);
        }catch (Exception e){
            resultStatus = "Fail. Please try again.";
        }
        return resultStatus;
    }
	
	public void run() {
	    boolean runStatus = true;
		while (runStatus){
			try {
				// in and out stream
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				System.out.println("Waiting for client input...");

				// buffer client input
				byte buffer[] = new byte[1024];
				in.read(buffer);
				String actionInput = new String(buffer);
				if (actionInput != null){
                    String actionRequest = actionInput.split(",")[0];
                    String actionTarget = actionInput.split(",")[1];
                    String actionArray = actionInput.split(",")[2];

                    // take action according to client action input - can change to switch case
                    String resultWriteString = "";
                    switch (actionRequest){
                        case "getMethod": resultWriteString = getMethod(actionTarget);
                            break;
                        case "deleteMethod": resultWriteString = deleteMethod(actionTarget);
                            break;
                        case "addMethod": resultWriteString = wordWorker.addMethod(actionTarget, actionArray);
                            break;
                    }

                    // send reply to client
                    out.write(resultWriteString.getBytes());
                    System.out.println("Response sent...");
                }

			}catch (Exception e) {
                runStatus = false;
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.out.println("Error: " + e);
                }
                System.out.println("Error hihi: " + e);
            }
		}

	
	}
}

public class Server {

	public static void main(String[] args) throws SocketException, IOException {
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
		    try {
                // accept client request
                System.out.println("Waiting for client..");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                // create new thread for each client connection
                System.out.println("Starting a new server thread..");
                new ServerThread(socket).start();
            }
            catch(Exception e){
		        System.out.println("Error: "+e);
            }
		}
	}

}
