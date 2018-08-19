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

public class Client {
    private JPanel JPane;
    private JButton editButton;
    private JList dictList;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton addButton;
    private JScrollPane scrollPane;
    private JFormattedTextField searchBar;
    private JPanel resultPane;
    private JLabel resultWord;
    private JPanel defPane;
    private JLabel statusLabel;
    private JLabel StatusHeading;
    private JPanel msgPane;
    private JTextField txtDisplay;
    private String wordToGetFromDict;

    public Client() throws UnknownHostException, IOException {
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hello from the UI side!!");
            }
        });
        // create GUI
        JFrame frame = new JFrame("Dictionary");
        frame.setBounds(100, 100, 700, 400);
        frame.setContentPane(JPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // connect to server socket
        Socket socket = new Socket("127.0.0.1", 9090);

        // Indicate connected on panel
        System.out.println("Connected to server...");
//        JLabel labelConnected = new JLabel("Connected to server...");
//        labelConnected.setFont(new Font("Andale Mono",  Font.BOLD, 14));
//        labelConnected.setBounds(72, 41, 350, 64);
        statusLabel.setText("Connected to server...");

        // i/o of client
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // search button action listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get text from search bar
                wordToGetFromDict = searchBar.getText();
                // labeling sent
                System.out.println("Sending word request..");
//                JLabel labelSent = new JLabel("Sending word request..");
//                labelSent.setFont(new Font("Andale Mono",  Font.BOLD, 14));
//                labelSent.setBounds(72, 41, 350, 64);
//                msgPane.add(labelSent);
                statusLabel.setText("Sending word request..");
                // send text to server
                try {
                    out.write(wordToGetFromDict.getBytes());
                    // get server message
                    byte[] response = new byte[1024];
                    in.read(response);

                    // print out server message
                    System.out.println("The definition of "+wordToGetFromDict+" is:");
                    System.out.println(new String(response).trim());

                    // put server message to panel
                    resultWord.setText("The definition of "+wordToGetFromDict+" is:");
                    JLabel labelDef = new JLabel(new String(response).trim());
                    labelDef.setFont(new Font("Tahoma",  Font.BOLD, 14));
                    labelDef.setBounds(0, 0, 350, 64);
                    defPane.add(labelDef);

                } catch (IOException e1) {
                    e1.printStackTrace();
//                    JLabel labelException = new JLabel("Error: "+ e1);
//                    labelException.setFont(new Font("Andale Mono",  Font.BOLD, 14));
//                    labelException.setBounds(72, 41, 350, 64);
//                    msgPane.add(labelException);
                    statusLabel.setText("Error: "+ e1);
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


        // close sockets
//        socket.close();

    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client();
    }
}
