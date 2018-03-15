package com.group15.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.sun.tools.doclint.Entity.le;

/**
 * Collection is a hashtable with all the usernames and the files which they uploaded, the file's "paths"
 * this way you can actually upload from the computer.
 */



public class DisplayFiles extends JFrame {


    JPanel jPanel = new JPanel();
    JPanel lower = new JPanel();
    JButton Exit;
    JButton DownloadButton;
    String heading[] = { "List of images available for download" };
    DefaultListModel model = new DefaultListModel();
    JList jlst = new JList(model);
    Map<String, File> collection = new HashMap<String, File>();


    public DisplayFiles() throws HeadlessException {

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
        Exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });

        DownloadButton = new JButton("Download");
        DownloadButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
               int A =  jlst.getSelectedIndex(); //TODO send for downloaing


            }
        });

        lower.add(DownloadButton);
    lower.add(Exit);


    }

    public void sendFile(String username, File file){

       // lbl.setBounds(10,10,670,250);
      //  lbl.setIcon(ResizeImage(path));
       //lbl jlst.add(path);

        String path = file.getAbsolutePath();
        String name = file.getName();

        int pos = jlst.getModel().getSize();
        model.add(pos, file);

        collection.put(username,file);   //TODO file is to be sent to other users.

        jPanel.revalidate(); //ADD THIS AS WELL
        jPanel.repaint();

    }


}
