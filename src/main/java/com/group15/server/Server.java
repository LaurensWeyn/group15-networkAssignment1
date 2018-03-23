package com.group15.server;

import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends Thread
{
    List<ClientHandler> clientList = Collections.synchronizedList(new ArrayList<>());
    private File storageFolder;
    private ArrayList<FileTransfer> fileDB = new ArrayList<>();

    public Server(File storageFolder)
    {
        super();
        this.storageFolder = storageFolder;
        if(storageFolder.exists())
        {
            if(!storageFolder.isDirectory())
                throw new IllegalStateException("storage folder not a directory");
            System.out.println("Cleaning up old file transfers...");
            for(File file : storageFolder.listFiles())
                file.delete();
            System.out.println("Transfer directory cleaned");
        }
        else
            storageFolder.mkdirs();
    }

    @Override
    public void run()
    {
        try
        {
            ServerSocket socket = new ServerSocket(12050);
            while (true)
            {
                Socket connection = socket.accept();
                ClientHandler newClient = new ClientHandler(this, connection);
                newClient.init();
            }
        }catch (IOException err)
        {
            err.printStackTrace();
        }
    }

    public List<ClientHandler> getClientList()
    {
        return clientList;
    }

    public void broadcast(Message msg)
    {
        //broadcasting message to all clients
        for (ClientHandler client:clientList) {
            client.sendMessage(msg);
        }
    }

    public void broadcast(FileTransfer msg)
    {
        //broadcasting message to all clients
        for (ClientHandler client:clientList) {
            client.sendMessage(msg);
        }
    }

    public void sendTo(String[] userList, Message msg)
    {
        for(String user:userList)//for every user that needs this message
        {
            for(ClientHandler client:clientList)//for every client that could be a relevant user
            {
                if(client.getUsername().equalsIgnoreCase(user))//if it matches
                {
                    client.sendMessage(msg);//send them the message
                    break;//and move to the next username
                }
            }
        }
    }

    public synchronized void storeFile(FileTransfer fileTransfer, byte[] data) throws IOException
    {
        fileTransfer.setId(fileDB.size());//assign ID based on position in database
        fileTransfer.setFile(new File(storageFolder, fileTransfer.getId() + ".bin"));//assign actual file in system
        fileDB.add(fileTransfer);//add file info to database
        Files.write(fileTransfer.getFile().toPath(), data);//store data in filesystem
        broadcast(fileTransfer);//announce to clients file is available
    }

    public FileTransfer getFile(int id)
    {
        return fileDB.get(id);
    }



    public static void main(String[] args)
    {
        new Server(new File("sharedFiles")).start();
        System.out.println("Main server thread started");
    }
}
