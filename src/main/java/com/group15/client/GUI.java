/*
This is the client and their user interface.
 */
package com.group15.client;


import com.group15.messageapi.MessageListener;
import com.group15.messageapi.PacketReader;
import com.group15.messageapi.PacketWriter;
import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

import static com.group15.messageapi.objects.Message.MsgType.serverMessage;
import static com.group15.messageapi.objects.Message.MsgType.userMessage;
import static java.awt.Color.black;
import static java.awt.Color.orange;

public class GUI extends JFrame implements ActionListener, MessageListener {

    /*
        Attributes
    */
    private JLabel userNamelbl;
    private JTextField addTxt, portTxt, sendtxt, userNametxt;
    private JButton start, logoutButton, onlineButton, sendButton, attachButton, clearButton, listButton;
    private JTextArea txtArea;
    private ArrayList<String> onlineList = new ArrayList<String>() ;/*Array with all the people onlineButton to be added*/
    private PacketWriter packetWriter; /*This is for the client*/
    private PacketReader packetReader; /*This is for the client*/
    private String username;
    private DisplayFiles df;




    /*Constructor*/
    GUI(String name, Socket socket)
	{
        super("Chat Room");
        try
        { packetWriter = new PacketWriter(socket.getOutputStream());
            packetReader = new PacketReader(socket.getInputStream(), this); }
            catch (IOException err){err.printStackTrace(); }
            this.username = name;

        /*Settting up the interface*/

            JPanel row1 = new JPanel(new BorderLayout());
	        row1.setBorder(new EmptyBorder(5,0,5,0));
            row1.setBackground(Color.black);
	        JPanel row1Panel=new JPanel(new GridLayout(0,1));
	        row1Panel.setBackground(Color.orange);
            JLabel l=new JLabel(new ImageIcon("src/main/java/com/group15/client/Pictures/LetsChat.jpg"));
	        l.setBorder(new EmptyBorder(5,5,5,5));
	        row1.add(l,"North");

        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel(new BorderLayout());
        row2.setBackground(Color.orange);
        row1Panel.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));

	    userNamelbl = new JLabel("Username: ", SwingConstants.LEFT);
	    userNamelbl.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	    userNamelbl.setFont(new Font("Comic Sans MS", Font.BOLD, 20));

            
	    userNametxt = new JTextField(username);
	    userNametxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
	    userNametxt.setBackground(Color.pink);

	    row1Panel.add(userNametxt);
        row2.add(userNamelbl);
        row2.add(userNametxt);
        row1Panel.add(row2);
        row1Panel.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        
      
        sendButton = new JButton(new ImageIcon("src/main/java/com/group15/client/Pictures/arrow.png"));
        sendButton.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        sendButton.addActionListener(new ActionListener()     
            {
                public void actionPerformed(ActionEvent e)
                    {
                        /*Checking if message sent is a private message or public message*/
                        String holder = sendtxt.getText();
                        String open = new String("{") ;
                        String users = new String("");

                        Boolean limited = false;

                        if (holder.length()>=3){

                            if (holder.substring(0,1).equals (open))  {


                                if (holder.contains("}")){
                                    limited = true ;

                                    holder = holder + "w";
                                    users = holder.substring(holder.indexOf("{") + 1, holder.indexOf("}")); }
                            }}

                        if (limited == true)
                        {
                            try { /*If private message use contructor with users*/
                                int close = holder.indexOf("}");
                                holder = holder.substring((close+2),holder.length()-1);
                                packetWriter.sendMessage(new Message(userMessage,holder,users));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } }
                        else{
                            try {/*If public message use contsructor which sends to all users*/
                                packetWriter.sendMessage(new Message(userMessage,holder));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                        sendtxt.setText("");
                    }
            });  
      
        attachButton = new JButton(new ImageIcon("src/main/java/com/group15/client/Pictures/attach.png"));
        attachButton.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        attachButton.addActionListener(new ActionListener(){
    
            public void actionPerformed(ActionEvent e)
                    {
                            JFileChooser fc = new JFileChooser();
                        int box = fc.showOpenDialog(null);
                        if ( box == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            if(file.length() > 2000000){ /*Checking of the file chossen is the correct size*/
                                JOptionPane.showMessageDialog(null, "Sorry, file too big" + "\n");


                            }
                            else{
                                /*File transmission sent successfully! " +"\n");*/
                                FileTransfer w = new FileTransfer(file, username);
                                try {
                                    packetWriter.transferFile(w);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }

                        listButton.doClick();
                    }
        });

        sendtxt = new JTextField("type....");
        sendtxt.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
	    sendtxt.setBackground(Color.pink);

        sendtxt.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String text = sendtxt.getText();  /*when user clicks enter on the keyboard to send a message*/
                sendButton.doClick();
            }
        });


	row3.add(sendtxt);        
	row3.add(sendButton,BorderLayout.EAST);
	row3.add(attachButton,BorderLayout.WEST);
            
	row1.add(row1Panel, BorderLayout.SOUTH);
	add(row1, BorderLayout.NORTH);
        
        
	/*The text area for messages to be displayed for users*/
	txtArea = new JTextArea("Welcome to Let's Chat!\n", 10, 20);
	txtArea.setBackground(Color.lightGray);
	JPanel centerPanel = new JPanel(new GridLayout(1,1));
	centerPanel.add(new JScrollPane(txtArea));
	txtArea.setEditable(false);
	txtArea.setFont(new Font("Arial",Font.BOLD,19));
	txtArea.setForeground(Color.black);
	txtArea.setLineWrap(true);
	txtArea.setWrapStyleWord(true);
	add(centerPanel, BorderLayout.CENTER);

	/*Shows the user is online*/
	start = new JButton("* online");
	start.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                    {  
                        //Checking if another user is making use of the same username
                        try {
                            packetWriter.login(username);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        df = new DisplayFiles(packetWriter);
                        userNametxt.setDisabledTextColor(Color.gray);
                        onlineButton.setEnabled(true);
                        logoutButton.setEnabled(true);
                        listButton.setEnabled(true);
                        clearButton.setEnabled(true);
                        start.setEnabled(false);
                        userNametxt.setEnabled(false);
                    }
            });
        start.setFont(new Font("Comic Sans MS", Font.BOLD, 15));



	logoutButton = new JButton("Log Out");
	logoutButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                    {
                        int dialogButton = JOptionPane.YES_NO_OPTION;
                        /*CHecking if user has not clicked log out by mistake*/
                        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave the chatroom?", "Chatroom", dialogButton);
                            if(dialogResult == 0) {
                                System.out.println("Yes option");
                                onlineButton.setEnabled(false); /*disable buttons*/
                                logoutButton.setEnabled(false);
                                listButton.setEnabled(false);
                                start.setEnabled(true);
                                clearButton.setEnabled(false);
                                String hold = new String (username+" has left the chatroom");
                                try {
                                    packetWriter.sendMessage(new Message(userMessage,hold)); /*tell server to boradcast the user is leaving the chatroom*/
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                                try {
                                    packetWriter.disconnect();  /* disconnecting the user*/
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                                dispose();
                            }

                            else {
                                 System.out.println("No Option"); /*user does not want to exit*/
                                 }

                    }
            });
            logoutButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            logoutButton.setEnabled(false);

            onlineButton = new JButton("Online Users");
            onlineButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                        {

                            try {
                                packetWriter.requestOnlineUsers();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                });  
            onlineButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            onlineButton.setEnabled(false);
            JPanel bottomPanel = new JPanel(new GridLayout(1,0));


        clearButton = new JButton("Clear"); /*clears the screen*/
        clearButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            { txtArea.setText("");
            }});


        listButton = new JButton("File List");
        listButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            { df.setVisible(true);
            }
        });

            clearButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            clearButton.setEnabled(false);
            listButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            listButton.setEnabled(false);

           bottomPanel.add(start);
           bottomPanel.add(onlineButton);
           bottomPanel.add(logoutButton);
           bottomPanel.add(clearButton);
           bottomPanel.add(listButton);

      
           JPanel BigPanel = new JPanel(new BorderLayout());
           BigPanel.setBorder(new EmptyBorder(5,0,5,0));
           BigPanel.setBackground(Color.black);
           BigPanel.add(row3,"North");
           BigPanel.add(bottomPanel, BorderLayout.SOUTH);
           setPreferredSize(new Dimension(600, 700));
           pack();

        GraphicsEnvironment graphicsE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = graphicsE.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - getWidth();
        int y = 0;
        setLocation(x-600, 50);  /*Positioning the window in the center when it's opened*/


        add(BigPanel, BorderLayout.SOUTH);  /*adding items to the bottom pane/*/
        row3.setBackground(Color.orange);
        bottomPanel.setBackground(Color.orange);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
        userNametxt.requestFocus();
        sendtxt.requestFocus();
        start.doClick();

            } /*End of constructor*/

    public void actionPerformed(ActionEvent e) {}

    /**
     *
     * Sections with all the methods to communicate with the server
     */

    @Override
    public void onLoginRequest(String username, String password)
    { /*to be implemented by the server*/ }

    @Override

    public void onMessage(Message msg)
    {

        SwingUtilities.invokeLater(() ->{
                    txtArea.append(msg.toString()+"\n");
        }); }

    @Override
    public void onFail(String error)
    {

        SwingUtilities.invokeLater(() ->{ JOptionPane.showMessageDialog(null, error);
        String[] t = new String[2];
            try {

                main(t);
                this.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public void onAck()

    {
        JOptionPane.showMessageDialog(null, "Successful connection!");

    }

    @Override
    public void onCorrupt()
    {
        JOptionPane.showMessageDialog(null, "Corrupt message from server");

    }
    @Override
    public void onFileTransferAvailable(FileTransfer fileTransfer)
    {
        df.sendFile(fileTransfer);
        /*announce new file to UI*/
        onMessage(new Message(serverMessage, fileTransfer + " is now available", "", fileTransfer.getTimestamp()));
    }

    @Override
    public void onFileTransferRequest(int fileID)
    {
        /*clients want to download the file
        //server*/

    }

    /*for file transgers*/
    @Override
    public void onFileTransfer(FileTransfer transfer, byte[] data)
    {
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(transfer.getFilename()));
            String extentionBits[] = transfer.getFilename().split("\\.");
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("given file", extentionBits[extentionBits.length - 1]);
            chooser.setDialogTitle("Save file from " + transfer.getUsername());
            chooser.setFileFilter(extensionFilter);

            chooser.showSaveDialog(null);
            File chosen = chooser.getSelectedFile();
            try {
                Files.write(chosen.toPath(), data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    @Override
    public void onOnlineUserListRequest()
    {
         /*for server*/
    }

    @Override
    public void onDisconnect() {
        /*for the server*/
    }

    /*showing the list of the users onlie after getting it from the server*/
    @Override
    public void onOnlineUserListResponse(String[]users) throws IOException {
        SwingUtilities.invokeLater(() ->{
        String Multiple = new String("There are " + users.length + " people in the onlineButton: \n");
        String Single = new String("You are the only user onlineButton.\n");

        if (users.length ==1)
        {
            JOptionPane.showMessageDialog(null, Single);
        }
        else {
            for (int i = 0; i < users.length; i++) {
                Multiple = ( Multiple + (i+1) + ". " + users[i] + "\n");
            }
            JOptionPane.showMessageDialog(null, Multiple);

        }});
    }



/*When GUI is run
*
*
* */
    public static void main(String[] args) throws IOException {

        /*setting up gui*/
       JFrame OpenScreen = new JFrame("Chat Room: Log In");
        JButton login;
        JPanel upper = new JPanel();
        JPanel middle = new JPanel();
        JPanel lower = new JPanel();
        JLabel l=new JLabel(new ImageIcon("src/main/java/com/group15/client/Pictures/LetsChat.jpg"));
        upper.add(l,"North");
        JLabel userNamelbl = new JLabel("Username: ", SwingConstants.LEFT);
        userNamelbl.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        userNamelbl.setFont(new Font("Comic Sans MS", Font.BOLD, 20));


        String randomUsername = "Anonymous" + (int)(Math.random() * 1000.0);
        JTextField userNametxt = new JTextField(randomUsername);
        userNametxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        userNametxt.setBackground(Color.pink);
        userNametxt.requestFocus();

        JLabel serverIP = new JLabel("Server IP: ");
        serverIP.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        serverIP.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        JTextField serverIPtxt = new JTextField("localhost");  
        serverIPtxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        serverIPtxt.setBackground(Color.pink);
        serverIPtxt.requestFocus();


        middle.add(userNamelbl, "left");
        middle.add(userNametxt, "left");
        middle.add(serverIP, "right");
        middle.add(serverIPtxt, "right");

        login = new JButton("Log In");
        login.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = userNametxt.getText();
                userNametxt.setDisabledTextColor(Color.gray);  /*user cannot change their username now*/
                OpenScreen.setVisible(false);
                GUI client = null;
                try {
                    client = new GUI(name,new Socket(serverIPtxt.getText(), 12050)); /*Our port number for users*/
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
                client.setVisible(true); /*user oly gets access tot he GUI if the log in is successful*/

            }
        });
        login.setFont(new Font("Comic Sans MS", Font.BOLD, 15));

        /*setting up the interface*/
        middle.add(userNamelbl);
        middle.add(userNametxt);
        lower.add(login);
        lower.setBackground(orange);
        middle.setBackground(orange);
        upper.setBackground(black);
        OpenScreen.setPreferredSize(new Dimension(600, 300));
        OpenScreen.pack();
        OpenScreen.setLocationRelativeTo(null);
        OpenScreen.add(upper, "North");
        OpenScreen.add(middle,"Center");
        OpenScreen.add(lower,"South");
        OpenScreen.setVisible(true);
        serverIPtxt.requestFocus(); /*for the user to edit*/


    }






}


