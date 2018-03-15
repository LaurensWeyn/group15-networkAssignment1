package com.laurens.hexcmd.read;

/**
 * Interface for HexCommand packet listeners.
 * Calls to these functions will be made by the packet reading thread.
 */
public interface PacketListener
{
    /**
     * Called when a full packet is successfully received.
     * Data in the DataReader is guaranteed to be valid for this packet until this method returns.
     * @param reader the DataReader from which packet data can be retrieved
     */
    void onComplete(DataReader reader);
    /**
     * Called when a packet error is detected and corrupt packet data is about to be discarded.
     * This call can be mostly ignored, except for perhaps reporting errors to the UI.
     * @param reader the DataReader from which partial packet data can be retrieved
     */
    void onFailure(DataReader reader);

    /**
     * Called when the client sending packets has disconnected.
     * No further calls will be made to the interface after this method has been called.
     * @param graceful true if the client had disconnected after a complete packet, false if the connection was lost mid-packet.
     */
    void onDisconnect(boolean graceful);
}
