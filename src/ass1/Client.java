package ass1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class InputNullException extends Exception
{
    public InputNullException() {}
}

public class Client {
    private JPanel JPane;
    private JList dictList;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton addButton;
    private JScrollPane scrollPane;
    private JFormattedTextField searchBar;
    private JLabel resultWord;
    private JLabel statusLabel;
    private JLabel StatusHeading;
    private JPanel msgPane;
    private JTextField txtDisplay;
    private String wordToGetFromDict;

    public Client() throws UnknownHostException, IOException {
        // connect to server socket
        Socket socket = new Socket("127.0.0.1", 9090);

        // create GUI
        JFrame frame = new JFrame("Dictionary");
        frame.setBounds(100, 100, 700, 400);
        frame.setContentPane(JPane);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                if (JOptionPane.showConfirmDialog(frame,
//                        "Are you sure to close this window?", "Closing?",
//                        JOptionPane.YES_NO_OPTION,
//                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//                        System.exit(0);
//                        problem with no option
                        try {
                            // close sockets
                            socket.close();
                            System.exit(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                            statusLabel.setText("Error: "+e);
                        }
//                }
                }
        });
        // Indicate connected on panel
        System.out.println("Connected to server...");
        statusLabel.setText("Connected to server...");

        // i/o of client
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        // remove button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get text from search bar
                wordToGetFromDict = searchBar.getText();
                // labeling sent
                System.out.println("Sending word request..");
                statusLabel.setText("Sending word request..");
                // send text to server
                String sendToServer = "";
                try {
                    sendToServer = "deleteMethod," + wordToGetFromDict + ",[]";
                    out.write(sendToServer.getBytes());
                    // get server message
                    byte[] response = new byte[1024];
                    in.read(response);

                    // print out server message
                    System.out.println("Deletion " + new String(response).trim());
                    JOptionPane.showMessageDialog(null, "Deletion " + new String(response).trim());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: "+ e1);
                }
//                JOptionPane.showMessageDialog(null, "Hello from the UI side!!");
            }
        });
        // search button action listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get text from search bar
                wordToGetFromDict = searchBar.getText();
                // labeling sent
                System.out.println("Sending word request..");
                statusLabel.setText("Sending word request..");
                // send text to server
                String sendToServer = "";
                try {
                    sendToServer = "getMethod," + wordToGetFromDict + ",[]";
                    out.write(sendToServer.getBytes());
                    // get server message
                    byte[] response = new byte[1024];
                    in.read(response);

                    // print out server message
                    System.out.println("The definition of "+wordToGetFromDict+" is:");
                    String definition = String.join("\n", new String(response).trim().split("-"));
                    System.out.println(definition);

                    // put server message to panel
                    JOptionPane.showMessageDialog(null, "The definition of "+wordToGetFromDict+" is: \n"+ definition);
//                    resultWord.setText("The definition of "+wordToGetFromDict+" is:");
//                    JLabel labelDef = new JLabel(new String(response).trim());
//                    defPane.add(labelDef);

                } catch (IOException e1) {
                    e1.printStackTrace();
                    statusLabel.setText("Error: "+ e1);
                }
            }
        });
        // add word to dictionary
        addButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                // Enter word and definition in window
                String word = JOptionPane.showInputDialog("Enter Word:");
                try {
                    if (word == null || word.length() == 0) {
                        throw new InputNullException();
                    } else {
                        String definition = JOptionPane.showInputDialog("Enter Definition:\n (Separate with '-' for multiple definition. e.g. Red-Round)");
                        // labeling sent
                        try {
                            if (definition == null || definition.length() == 0) {
                                throw new InputNullException();
                            } else {
                                System.out.println("Sending word request..");
                                statusLabel.setText("Sending word request..");
                                // send text to server
                                String sendToServer = "";
                                try {
                                    sendToServer = "addMethod," + word + "," + definition;
                                    out.write(sendToServer.getBytes());
                                    // get server message
                                    byte[] response = new byte[1024];
                                    in.read(response);

                                    // print out server message and put to panel
                                    System.out.println(new String(response).trim());
                                    JOptionPane.showMessageDialog(null, "Adding " + new String(response).trim());

                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                    statusLabel.setText("Error: " + e1);
                                }
                            }
                        } catch (InputNullException ex1) {
                            JOptionPane.showMessageDialog(null, "Input can't be null, please try again.");
                        }
                    }
                } catch (InputNullException ex) {
                    JOptionPane.showMessageDialog(null, "Input can't be null, please try again.");
                }
            }
        });
        //prevent null input
//        while (wordToGetFromDict.length() == 0) {
//            // write client message
//            System.out.println("Enter word to retrieve from the dictionary:");
//            Scanner keyboard = new Scanner(System.in);
//            wordToGetFromDict = keyboard.nextLine();
//        }



    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client();
    }
}
