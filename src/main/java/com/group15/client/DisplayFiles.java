package com.group15.client;

import com.group15.messageapi.PacketWriter;
import com.group15.messageapi.objects.FileTransfer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * UI for displaying files available for download
 */
public class DisplayFiles extends JFrame {


    private JPanel jPanel = new JPanel();
    private JPanel lower = new JPanel();
    private JButton Exit;
    private JButton DownloadButton;
    private DefaultListModel model = new DefaultListModel();
    private JList jlst = new JList(model);


    /*
    Displays a list of files available for download sent by other chat users
    Also allows you to download the specific file from the list into your computer
     */
    public DisplayFiles(PacketWriter packetWriter) throws HeadlessException {

        setVisible(false);
        setTitle("Images for downloading...");
        jlst.setSize(300,300);
        jPanel.add(jlst);
        jlst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        add(jPanel,"North");
        add(lower, "South");
        setPreferredSize(new Dimension(400, 400));
        pack();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - getWidth();
        int y = 0;
        setLocation(x-150, 200);


        Exit = new JButton("Hide list");
        Exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e){
                setVisible(false);
            }

        });

        DownloadButton = new JButton("Download");
        DownloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                FileTransfer fileTransfer = (FileTransfer)jlst.getSelectedValue();
                try {
                    packetWriter.requestFileTransfer(fileTransfer);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        });

        lower.add(DownloadButton);
        lower.add(Exit);

    }


    /*
     sends a file to the rest of the users in the chat
     */
    public void addFile(FileTransfer file){

        int pos = jlst.getModel().getSize();
        model.add(pos, file);

        jPanel.revalidate();
        jPanel.repaint();

    }


}
