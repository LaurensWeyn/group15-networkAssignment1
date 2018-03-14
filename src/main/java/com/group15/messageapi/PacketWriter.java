package com.group15.messageapi;

import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;
import com.laurens.hexcmd.write.HexCmdTransmitter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Sends messaging packets by encoding them through HexCommand. <br>
 * All send functions are synchronised, so this implementation is thread safe,
 * as long as the underlying HexCmdTransmitter or OutputStream is not used by any other thread during message transmission.
 */
public class PacketWriter
{
    private HexCmdTransmitter transmitter;

    public PacketWriter(OutputStream outputStream)
    {
        this(new HexCmdTransmitter(outputStream));
    }

    public PacketWriter(HexCmdTransmitter transmitter)
    {
        this.transmitter = transmitter;
    }


    /**
     * Send a text message.
     * @param message The message to send
     * @throws IOException on network errors
     */
    public synchronized void sendMessage(Message message)throws IOException
    {
        transmitter.sendByte('T', 2);
        transmitter.sendString('M', message.getText());
        transmitter.sendString('U', message.getUser());
        transmitter.sendByte('C', message.getMsgType().ordinal());
        transmitter.sendLong('D', message.getTimestamp().getTime());
        transmitter.endPacket();
    }

    /**
     * Sends a login request to the server. Should be the first message from a client.
     * @param username The username to use
     * @param password The password for that username (if any)
     * @throws IOException  on network errors
     */
    public synchronized void login(String username, String password)throws IOException
    {
        if(password == null)
            password = "";
        transmitter.sendByte('T', 1);
        transmitter.sendString('M', username);
        transmitter.sendString('P', password);
        transmitter.endPacket();
    }
    /**
     * Sends a login request
     * @param username The username to use
     * @throws IOException on network errors
     */
    public synchronized void login(String username)throws IOException
    {
        login(username, "");
    }

    /**
     * Requests a file from the server
     * @param fileTransfer the metadata of the file to transfer (received from server)
     * @throws IOException on network errors
     */
    public synchronized void requestFileTransfer(FileTransfer fileTransfer) throws IOException
    {
        transmitter.sendByte('T', 4);
        transmitter.sendInt('I', fileTransfer.getId());
        transmitter.endPacket();
    }

    /**
     * Transfer a file over the network
     * @param fileTransfer the metadata of the file to transfer
     * @throws IOException on network errors
     */
    public synchronized void transferFile(FileTransfer fileTransfer) throws IOException
    {
        transmitter.sendByte('T', 5);
        transmitter.sendLong('S', fileTransfer.getLength());
        byte[] data = Files.readAllBytes(fileTransfer.getFile().toPath());
        transmitter.sendBytes('B', data);
        transmitter.sendLong('D', fileTransfer.getTimestamp().getTime());
        transmitter.endPacket();
    }

    /**
     * Request the list of online users from the server
     * @throws IOException on network errors
     */
    public synchronized void requestOnlineUsers() throws IOException
    {
        transmitter.sendByte('T', 7);
        transmitter.endPacket();
    }

    /**
     * Send the list of active users to a client
     * @param usernames the usernames of users currently online
     * @throws IOException on network errors
     */
    public synchronized void sendOnlineUserList(ArrayList<String> usernames)throws IOException
    {
        transmitter.sendByte('T', 6);
        transmitter.sendString('M', String.join("\n", usernames));
        transmitter.endPacket();
    }

    /**
     * Send a login accept packet to the client.
     * @throws IOException on network errors
     */
    public synchronized void sendAck()throws IOException
    {
        transmitter.sendByte('T', 0);
        transmitter.sendByte('A', 1);
        transmitter.endPacket();
    }

    /**
     * Send a login denied packet to the client. Also used to kick a user currently active in the chat for some reason.
     * @param errorMsg A user readable reason for being denied access
     * @throws IOException on network errors
     */
    public synchronized void sendNack(String errorMsg)throws IOException
    {
        transmitter.sendByte('T', 0);
        transmitter.sendByte('A', 0);
        transmitter.sendString('M', errorMsg);
        transmitter.endPacket();
    }
}
