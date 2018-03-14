package com.group15.server;

import com.group15.messageapi.objects.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread
{
    static ArrayList<ClientHandler> clientList = new ArrayList<>();
    static Message msg;
    @Override
    public void run()
    {
        try
        {
            ServerSocket socket = new ServerSocket(1024);
            while (true)
            {
                Socket connection = socket.accept();
                ClientHandler newClient = new ClientHandler(this, connection);
                clientList.add(newClient);
                newClient.start();
            }
        }catch (IOException err)
        {
            err.printStackTrace();
        }



    }

    public void broadcast(Message msg)
    {
        //broadcasting message to all clients
        for (ClientHandler client:clientList) {
            client.sendMessage(msg);
        }
    }

    public static void main(String[] args)
    {
        new Server().start();
    }
}
