package com.group15.messageapi;

import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;
import com.laurens.hexcmd.read.DataReader;
import com.laurens.hexcmd.read.HexCmdReceiver;
import com.laurens.hexcmd.read.PacketListener;
import com.laurens.hexcmd.read.readers.DynamicDataReader;

import java.io.InputStream;
import java.util.Date;

/**
 * Translates HexCmd packets into messaging packets as per the messaging protocol.
 * Message packets are decoded and given to a MessageListener for processing.
 */
public class PacketReader implements PacketListener
{

    private MessageListener listener;

    /**
     * 'All in one' constructor. Given an inputStream, this initialises a reader and runs a receiver on a separate thread.
     * Use the other constructor for finer control.
     * @param inputStream the stream from which to read (typically from a network socket)
     * @param listener the listener to take in the read message packets
     */
    public PacketReader(InputStream inputStream, MessageListener listener)
    {
        this(listener);
        //init source of data
        DynamicDataReader reader = new DynamicDataReader();
        reader.addPacketListener(this);

        //init thread to read data
        HexCmdReceiver receiver = new HexCmdReceiver(inputStream, reader);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
    }

    /**
     * Basic constructor. Note that this class needs to be added to a reader as a packet listener in order for it to be useful.
     * @param listener the listener to take in the read message packets
     */
    public PacketReader(MessageListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onComplete(DataReader reader)
    {
        switch(reader.getByte('T'))
        {
            case 0:
                if(reader.getByte('A') == 1)
                    listener.onAck();
                else
                    listener.onFail(reader.getString('M'));
                break;
            case 1:
                listener.onLoginRequest(reader.getString('M'), reader.getString('P'));
                break;
            case 2:
                listener.onMessage(new Message(Message.MsgType.values()[reader.getByte('C')],
                        reader.getString('M'),
                        reader.getString('U'),
                        new Date(reader.getLong('D'))));
            break;
            case 3:
                listener.onFileTransferAvailable(new FileTransfer(reader.getString('M'),
                        reader.getLong('S'),
                        reader.getInt('I'),
                        new Date(reader.getLong('D')),
                        reader.getString('U')));
                break;
            case 4:
                listener.onFileTransferRequest(reader.getInt('I'));
                break;
            case 5:
                listener.onFileTransfer(new FileTransfer(reader.getString('M'),
                        reader.getLong('S'),
                        new Date(reader.getLong('D'))), reader.getBytes('B'));
                break;
            case 6:
                listener.onOnlineUserListResponse(reader.getString('M').split("\n"));
                break;
            case 7:
                listener.onOnlineUserListRequest();
                break;
        }
    }

    @Override
    public void onFailure(DataReader dataReader)
    {
        listener.onCorrupt();
    }
}
