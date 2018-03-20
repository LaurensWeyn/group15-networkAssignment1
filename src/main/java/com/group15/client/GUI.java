/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group15.client;


import com.group15.messageapi.MessageListener;
import com.group15.messageapi.PacketReader;
import com.group15.messageapi.PacketWriter;
import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFileChooser;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.group15.messageapi.objects.Message.MsgType.serverMessage;
import static com.group15.messageapi.objects.Message.MsgType.userAction;
import static com.group15.messageapi.objects.Message.MsgType.userMessage;
import static java.awt.Color.black;
import static java.awt.Color.orange;

public class GUI extends JFrame implements ActionListener, MessageListener {

    //Attributes
    private JLabel userNamelbl;
    private JTextField addTxt, portTxt, sendtxt, userNametxt;
    private JButton start, logout, online, sendButton, attachButton, clearButton, listButton;
    private JTextArea txtArea;
    private int onlineNum = 5; //**To be replaced with actual number
    private ArrayList<String> onlineList = new ArrayList<String>() ;//**Array with all the people online to be added
    private PacketWriter packetWriter; //This is for the client
    private PacketReader packetReader; //This is for the client
    private String username;

    JLabel image;
    DisplayFiles df;




    //Constructor
    GUI(String name, Socket socket)

	{
        super("Chat Room");
        try
        { packetWriter = new PacketWriter(socket.getOutputStream());
            packetReader = new PacketReader(socket.getInputStream(), this); }

            catch (IOException err){err.printStackTrace(); }
            this.username = name;
            JPanel topPanel = new JPanel(new BorderLayout()); //top half
	        topPanel.setBorder(new EmptyBorder(5,0,5,0));
            topPanel.setBackground(Color.black);
	        JPanel topPanelPanel=new JPanel(new GridLayout(0,1));
	        topPanelPanel.setBackground(Color.orange);
            JLabel l=new JLabel(new ImageIcon("src/main/java/com/group15/client/Pictures/LetsChat.jpg"));
	        l.setBorder(new EmptyBorder(5,5,5,5));




	    topPanel.add(l,"North");
      

        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel(new BorderLayout());
      

        row2.setBackground(Color.orange);
		
	/*

	JPanel row1 = new JPanel();
	row1.setBackground(Color.orange);

	addTxt = new JTextField(socket.gethost);
	addTxt.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	addTxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        addTxt.setBackground(Color.pink);

	portTxt = new JTextField("" + port);
	portTxt.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	portTxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        portTxt.setBackground(Color.pink);
	portTxt.setHorizontalAlignment(SwingConstants.RIGHT)
      
	JLabel addLbl=new JLabel("IP Address:  ");
	addLbl.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	addLbl.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
      
	row1.add(addLbl);
	row1.add(addTxt);
	row1.add(new JLabel(""));

	addLbl=new JLabel("Port Number:  ");
	addLbl.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	addLbl.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
     row1.add(portTxt);
	row1.add(addLbl);

	topPanelPanel.add(row1);

	*/




	// adds the ip address an port field to the GUI

	topPanelPanel.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	// the Label and the TextField
      
	userNamelbl = new JLabel("Username: ", SwingConstants.LEFT);
	userNamelbl.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
	userNamelbl.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
	//topPanelPanel.add(userNamelbl); 
            
	userNametxt = new JTextField(username);
	userNametxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
	userNametxt.setBackground(Color.pink);
	topPanelPanel.add(userNametxt);
      
        row2.add(userNamelbl);
        row2.add(userNametxt);
        topPanelPanel.add(row2);
        topPanelPanel.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        
      
        sendButton = new JButton(new ImageIcon("src/main/java/com/group15/client/Pictures/arrow.png"));
        sendButton.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        sendButton.addActionListener(new ActionListener()     
            {
                public void actionPerformed(ActionEvent e)
                    {
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
                            try {
                                int close = holder.indexOf("}");
                                holder = holder.substring((close+2),holder.length()-1);
                                packetWriter.sendMessage(new Message(userMessage,holder,users));  //TODO server side for directed msgs
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } }
                        else{
                            try {
                               // int close = holder.indexOf("}");
                              //  System.out.println(holder = holder.substring((close+2),holder.length()));
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
                        /*JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                           File selectedFile = fileChooser.getSelectedFile();*/



                            JFileChooser fc = new JFileChooser();    //Todo limit file size - DONE
                        int box = fc.showOpenDialog(null);
                        if ( box == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            if(file.length() > 2000000){
                                JOptionPane.showMessageDialog(null, "Sorry, file too big" + "\n");


                            }
                            else{
                                //txtArea.append("File " + file.getName() + " sent successfully! " +"\n");
                                FileTransfer w = new FileTransfer(file, username);
                                df.sendFile(w);
                                try {
                                    packetWriter.transferFile(w);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }

                                //ADD THIS AS WELL

                        }
                    }       
                    /*JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int returnValue = jfc.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
                        
                    */ 
                    
		   
            
            
        });
        
      
        //sendButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        sendtxt = new JTextField("type....");
        sendtxt.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
	sendtxt.setBackground(Color.pink);


	row3.add(sendtxt);        
        row3.add(sendButton,BorderLayout.EAST);
        row3.add(attachButton,BorderLayout.WEST);
            
        topPanel.add(topPanelPanel, BorderLayout.SOUTH);
	add(topPanel, BorderLayout.NORTH);
        
        
	// The CenterPanel which is the chat room
	txtArea = new JTextArea("Welcome to Let's Chat!\n", 10, 20); //TODO JEditorPane, random color  for each user
           /* {
		Image image =new ImageIcon("/Users/winfredamazvidza/Desktop/chatProgram 2/src/main/java/com/group15/happy/Pictures/vintage.jpg").getImage();// imageIcon.getImage();
                Image grayImage = GrayFilter.createDisabledImage(image);
                    {
                        setOpaque(false);
                    }
                public void paint(Graphics g) 
                {	  
                    g.drawImage(grayImage, 0, 0,getWidth(),getHeight(), this);
                    super.paint(g);
		}};*/
           txtArea.setBackground(Color.lightGray);
	JPanel centerPanel = new JPanel(new GridLayout(1,1));
	centerPanel.add(new JScrollPane(txtArea));
	txtArea.setEditable(false);
	txtArea.setFont(new Font("Arial",Font.BOLD,19));
	txtArea.setForeground(Color.black);
	txtArea.setLineWrap(true);
	txtArea.setWrapStyleWord(true);
	add(centerPanel, BorderLayout.CENTER);

	// log in button
	start = new JButton("start");
	start.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                    {  
                        //Checking if another user is making use of the same username
                        //String name = userNametxt.getText();
                        try {
                            packetWriter.login(username);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        df = new DisplayFiles(packetWriter);
                        userNametxt.setDisabledTextColor(Color.gray);
                        online.setEnabled(true);
                        logout.setEnabled(true);
                        listButton.setEnabled(true);
                        clearButton.setEnabled(true);
                        start.setEnabled(false);


                        /*try {
                            packetWriter.sendMessage(new Message(Message.MsgType.serverMessage,  username+ " joined the chat room!\n"));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }*/

                       // userNametxt.enableInputMethods(false);
                        userNametxt.setEnabled(false);



                        /*Boolean check = isValid(name);
                            if (check != false)
                            {
                                //Checking if Connection has beed extablished
                                Boolean logInCheck = ConnectionCheck();
                                    if (logInCheck==true)
                                    {
                                        online.setEnabled(true);
                                        logout.setEnabled(true);
                                        txtArea.append(name+ " joined the chat room!\n");
                                        login.setEnabled(false);
                                        userNametxt.setText(name);
                                    }
                                    else
                                    {      
                                        JOptionPane.showMessageDialog(null, "Sorry, connection failed.\n Please try again.\n" + getonlineUsers());
                                    } 
                            }
                            else 
                            {
                                JOptionPane.showMessageDialog(null, "Sorry, username taken\n Please try again.\n" + getonlineUsers());
                                userNametxt.setText("");
                            }*/
                    }
            });
        start.setFont(new Font("Comic Sans MS", Font.BOLD, 15));


        // logout button
	logout = new JButton("Log Out");
	logout.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                    {
                        int dialogButton = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave the chatroom?", "Chatroom", dialogButton);
                            if(dialogResult == 0) {
                                System.out.println("Yes option");
                                online.setEnabled(false);
                                logout.setEnabled(false);
                                listButton.setEnabled(false);
                                start.setEnabled(true);
                                clearButton.setEnabled(false);
                                try {

                                    packetWriter.disconnect();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                                dispose();


                            }
                            else {
                                 System.out.println("No Option");
                                 }

                    }
            });
            logout.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            logout.setEnabled(false);		// you have to login before being able to logout

            online = new JButton("Online");
            online.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                        {
                            //JOptionPane.showMessageDialog(null, "There " + onlineNum+" people in the chat room: \n" + getonlineUsers());
                            try {
                                packetWriter.requestOnlineUsers();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                });  
            online.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            online.setEnabled(false);		// you have to login before being able to know online
                                                // text to know the number of people online
            JPanel bottomPanel = new JPanel(new GridLayout(1,0));
            //    bottomPanel.add(row3,new GridLayout());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            { txtArea.setText("");
            }
        });


        listButton = new JButton("List");
        listButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            { df.setVisible(true);
            }
        });

            clearButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            clearButton.setEnabled(false);		// you have to login before being able to logout
            listButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            listButton.setEnabled(false);		// you have to login before being able to logout
           bottomPanel.add(start);
           bottomPanel.add(online);
           bottomPanel.add(logout);
           bottomPanel.add(clearButton);
           bottomPanel.add(listButton);

     
		//add(bottomPanel, BorderLayout.SOUTH);
		//bottomPanel.setBackground(Color.orange);
      
           JPanel CavePanel = new JPanel(new BorderLayout());
           CavePanel.setBorder(new EmptyBorder(5,0,5,0));
           CavePanel.setBackground(Color.black);
           CavePanel.add(row3,"North");
           CavePanel.add(bottomPanel, BorderLayout.SOUTH);
           setPreferredSize(new Dimension(600, 700));
           pack();
        //setLocationRelativeTo(null);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - getWidth();
        int y = 0;
        setLocation(x-600, 50);


        add(CavePanel, BorderLayout.SOUTH);
        row3.setBackground(Color.orange);
        bottomPanel.setBackground(Color.orange);
		
           setDefaultCloseOperation(EXIT_ON_CLOSE);
           //setSize(600, 700);
           setVisible(true);
           userNametxt.requestFocus();
           sendtxt.requestFocus();
           start.doClick();

            }

    private Boolean ConnectionCheck(){
        Boolean logInCheck = true; //** to be changed
        return logInCheck;
    }
    
    //Action Performed
    public void actionPerformed(ActionEvent e) {}

    private String getonlineUsers(){
        String onlineUsers = new String("");
        String newName = new String("");
            for (String i:onlineList) {
                newName = i + "\n";
                onlineUsers = onlineUsers + newName;
            }
        return onlineUsers;
    }
 
    private Boolean isValid (String name){
     Boolean check = true;  
        if (onlineList.isEmpty() ==false){
            for (String i:onlineList) {
                if (i.toLowerCase().equals(name.toLowerCase())) //compared in lowercase
                    {check = false;}
            }
        }
       onlineList.add(name); //Append List
       return check;
    }

    /**
     *
     * Sections with all the methods to communicate with the server
     */

    @Override
    public void onLoginRequest(String username, String password)
    {

    }

    @Override

    public void onMessage(Message msg)
    {

        SwingUtilities.invokeLater(() ->{


                    txtArea.append(msg.toString()+"\n");





        });


    }

    @Override
    public void onFail(String error)
    {
        //txtArea.append(error);
        //onFail("Corrupt message from server \n");
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
        System.out.println("notified of " + fileTransfer);
        df.sendFile(fileTransfer);
        //announce new file to UI
        onMessage(new Message(serverMessage, fileTransfer + " is now available", "", fileTransfer.getTimestamp()));
    }

    @Override
    public void onFileTransferRequest(int fileID)
    {
        //clients want to download the file
        //server

    }
    @Override
    public void onFileTransfer(FileTransfer transfer, byte[] data)
    {
        System.out.println("transferring " + transfer);
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();//TODO jFilechooser save with extension
            chooser.setDialogTitle("Save file from " + transfer.getUsername());

            chooser.showSaveDialog(null);
            File chosen = chooser.getSelectedFile();
            System.out.println("writing file");
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
         //for server
    }

    @Override
    public void onDisconnect() {
        //for the server
    }

    @Override
    public void onOnlineUserListResponse(String[]users) throws IOException {
        SwingUtilities.invokeLater(() ->{
        String Multiple = new String("There are " + users.length + " people in the online: \n");
        String Single = new String("You are the only user online.\n");

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


    //Running Instructions for now

    public static void main(String[] args) throws IOException {


       // GUI client = new GUI(new Socket("localhost", 1024));
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

        JTextField userNametxt = new JTextField("Anonymous");  //TODO 1-1000
        userNametxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        userNametxt.setBackground(Color.pink);
        userNametxt.requestFocus();

        JLabel serverIP = new JLabel("Server IP: ");
        serverIP.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        serverIP.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        JTextField serverIPtxt = new JTextField("localhost");  //TODO how which one should we place ad default
        serverIPtxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        serverIPtxt.setBackground(Color.pink);
        serverIPtxt.requestFocus();


        middle.add(userNamelbl, "left");//moved
        middle.add(userNametxt, "left");//moved

        middle.add(serverIP, "right");
        middle.add(serverIPtxt, "right");

        login = new JButton("Log In");
        login.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = userNametxt.getText();
                userNametxt.setDisabledTextColor(Color.gray);
                OpenScreen.setVisible(false);
                GUI client = null;
                try {
                   // client = new GUI(name,new Socket("localhost", 1024)); //TODO textbox for user to enter IP add for the server
                    client = new GUI(name,new Socket(serverIPtxt.getText(), 1024)); //TODO textbox for user to enter IP add for the server
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                client.setVisible(true);

            }
        });
        login.setFont(new Font("Comic Sans MS", Font.BOLD, 15));

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
        userNametxt.requestFocus();


    }






}


