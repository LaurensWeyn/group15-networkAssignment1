package com.group15.messageapi.objects;

import java.util.Date;

/**
 * Holds a user/status message sent or received in the chat room.
 */
public class Message
{
    private MsgType msgType;
    private String text;
    private String user;
    private Date timestamp;

    public Message(MsgType msgType, String text, String user, Date timestamp)
    {
        this.msgType = msgType;
        this.text = text;
        this.user = user;
        this.timestamp = timestamp;
    }

    public Message(MsgType msgType, String text, String user)
    {
        this.msgType = msgType;
        this.text = text;
        this.user = user;
        this.timestamp = new Date();//now
    }

    /**
     * Constructor sufficient for client to server messages, or server status messages (which have no user)
     * @param msgType the type of message
     * @param text the text of the message
     */
    public Message(MsgType msgType, String text)
    {
        this.msgType = msgType;
        this.text = text;
        this.user = "";//user blank for client->server messages
        this.timestamp = new Date();//now
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * Get the type of this message
     * @return The message type
     */
    public MsgType getMsgType()
    {
        return msgType;
    }


    /**
     * Get the text contents of this message
     * @return The message text
     */
    public String getText()
    {
        return text;
    }

    /**
     * The user who sent this message.
     * Field only relevant client-side; server-side should use login information to determine user (to prevent user impersonation).
     * On client to server side messages, user may even be left blank.
     * @return the username, or the empty string if a client to server message
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Get the time at which this message was originally sent.
     * @return the timestamp of this message
     */
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * Very basic ASCII formatted message. UI implementations may want to use something fancier, but this is sufficient for console debugging.
     * @return a textual representation of this message
     */
    @Override
    public String toString()
    {
        String string = timestamp + " ";
        switch(msgType)
        {
            case serverMessage:
                string += text;
                break;
            case userAction:
                string += "*" + user + " " + text + "*";
                break;
            case userMessage:
                string += "<" + user + ">: " + text;
                break;
        }
        return string;
    }

    public enum MsgType
    {
        userMessage,
        userAction,
        serverMessage
    }
}
