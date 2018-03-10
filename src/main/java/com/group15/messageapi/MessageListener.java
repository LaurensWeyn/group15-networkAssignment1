package com.group15.messageapi;

import com.group15.messageapi.objects.FileTransfer;
import com.group15.messageapi.objects.Message;

/**
 * A listener for chat messages.
 * Server-side implementation note: Note that calls to these functions do not include information about the client.
 * This should be tracked by the MessageListener implementation on initialisation.
 */
public interface MessageListener
{
    /**
     * Called on receiving a text message.
     * @param msg the message
     */
    void onMessage(Message msg);

    /**
     * Called on being denied login or being kicked from a server (e.g. for sending messages before logging in)
     * @param error A human-readable error message
     */
    void onFail(String error);

    /**
     * Called on a successful login.
     */
    void onAck();

    /**
     * Called on any network errors.
     */
    void onCorrupt();

    /**
     * Called when a client requests a login (server-side only)
     * @param username the username with which the user is trying to login
     * @param password the user's provided password, or the empty string if none is provided. Can be ignored.
     */
    void onLoginRequest(String username, String password);

    /**
     * Called when a file transfer is available from the server (client-side only)
     * @param fileTransfer metadata about the file to be transferred
     */
    void onFileTransferAvailable(FileTransfer fileTransfer);

    /**
     * Called when a client requests a file transfer (server-side only)
     * @param fileID the ID of the file requested to be transferred
     */
    void onFileTransferRequest(int fileID);

    /**
     * Called when a file is being received (both client and server side)
     * @param transfer metadata about the file being transferred
     * @param data the file contents
     */
    void onFileTransfer(FileTransfer transfer, byte[] data);

    /**
     * Called when the server sends a list of online users (client-side only)
     * @param users an array of users currently online
     */
    void onOnlineUserListResponse(String[] users);

    /**
     * Called when a client requests the list of active users (server-side only)
     */
    void onOnlineUserListRequest();
}

