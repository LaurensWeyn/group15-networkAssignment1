package com.group15.server;

import com.group15.client.GUI;
import com.group15.messageapi.MessageListener;
import com.group15.messageapi.PacketReader;
import com.group15.messageapi.PacketWriter;
import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static com.group15.messageapi.objects.Message.MsgType.serverMessage;
import static com.group15.messageapi.objects.Message.MsgType.userMessage;

public class ClientHandler implements MessageListener
{
    private Socket socket;
    private PacketReader packetReader;
    private PacketWriter packetWriter;
    private String username = null;
    private Server parentServer;

    public ClientHandler(Server parentServer, Socket socket)
    {
        this.socket = socket;
        this.parentServer = parentServer;
    }

    public void init()
    {
        try
        {
            packetWriter = new PacketWriter(socket.getOutputStream());
            packetReader = new PacketReader(socket.getInputStream(), this);
        }catch (IOException err)
        {
            err.printStackTrace();
        }
    }

    public void sendMessage(Message msg)
    {
        try {
            packetWriter.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(FileTransfer fileTransfer)
    {
        try {
            packetWriter.sendFileTransferAvailable(fileTransfer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message msg)
    {
        if(username == null) {
            try {
                packetWriter.sendNack("Cannot send messages without being logged in.");
                socket.close();
                parentServer.getClientList().remove(this);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        msg.setUser(username);
        parentServer.broadcast(msg);

    }

    @Override
    public void onFail(String error)
    {
        //server cannot log into client
        parentServer.getClientList().remove(this);
        username = null;
    }

    @Override
    public void onAck()
    {

    }

    @Override
    public void onCorrupt()
    {
        //inform that the message is gabbage
        try {
            packetWriter.sendNack("Invalid packet");
            parentServer.getClientList().remove(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoginRequest(String username, String password)
    {
        this.username = username;
        int found = 0;



        for (ClientHandler check: parentServer.clientList)
        {
            if (check.username.equals(username)){ found++;}

        }


        if (found == 1) {
            try {
                packetWriter.sendNack("Username is taken, please try again");
            } catch (IOException e) {
                e.printStackTrace();

            }}
            else{
        try
        {
            packetWriter.sendAck();
            parentServer.clientList.add(this);
            parentServer.broadcast(new Message(Message.MsgType.serverMessage, username + " has joined the chatroom!"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }}
    }

    @Override
    public void onFileTransferAvailable(FileTransfer fileTransfer)
    {
        //server never gets this
    }

    @Override
    public void onFileTransferRequest(int fileID)
    {
        //send file to client
        try
        {
            FileTransfer fileTransfer = parentServer.getFile(fileID);
            packetWriter.transferFile(fileTransfer);
        }catch(IOException | IndexOutOfBoundsException err)
        {
            err.printStackTrace();
        }
    }

    @Override
    public void onFileTransfer(FileTransfer transfer, byte[] data)
    {
        try
        {
            transfer.setUsername(username);//remember who sent this
            parentServer.storeFile(transfer, data);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onOnlineUserListResponse(String[] users) {
        //server never gets this
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void onOnlineUserListRequest()
    {
        if(username == null)
            return;
        ArrayList<String> users = new ArrayList<>();
         //what clients will send to the server
        for (ClientHandler client: parentServer.getClientList()){
            users.add(client.getUsername());
        }
        try {
            packetWriter.sendOnlineUserList(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect() {

        parentServer.getClientList().remove(this);
        try {
            packetWriter.sendMessage(new Message(serverMessage,username + " left the chatroom\n" ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
