package com.group15.server;

import com.group15.messageapi.MessageListener;
import com.group15.messageapi.PacketReader;
import com.group15.messageapi.PacketWriter;
import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread implements MessageListener
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

    @Override
    public void run()
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

    @Override
    public void onMessage(Message msg)
    {
        if(username == null) {
            try {
                packetWriter.sendNack("Cannot send messages without being logged in.");
                socket.close();
                Server.clientList.remove(this);
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
        Server.clientList.remove(this);
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
            Server.clientList.remove(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoginRequest(String username, String password)
    {
        this.username = username;
        //TODO check if username isn't taken
        try
        { packetWriter.sendAck();
            parentServer.broadcast(new Message(Message.MsgType.serverMessage, username + " has joined the chatroom!"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileTransferAvailable(FileTransfer fileTransfer)
    {

    }

    @Override
    public void onFileTransferRequest(int fileID)
    {
      //clients want to download the file

    }

    //TODO create "database" via a hashmap if ids and file transfer metadata, ids are assigned by the server.
    @Override
    public void onFileTransfer(FileTransfer transfer, byte[] data)
    {

    }

    @Override
    public void onOnlineUserListResponse(String[] users) {
      //what clients get


    }

    public String getUsername() {
        return username;
    }

    @Override
    public void onOnlineUserListRequest()
    {
        ArrayList<String> users = new ArrayList<>();
         //what clients will send to the server
        for (ClientHandler client: Server.clientList){
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
        Server.clientList.remove(this);
    }
}
