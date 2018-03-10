package com.group15.messageapi.objects;

import java.io.File;
import java.util.Date;

public class FileTransfer
{
    private String filename;
    private long length;
    private int id;
    private File file;
    private Date timestamp;
    private String username;

    public FileTransfer(String filename, long length, int id, Date timestamp, String username)
    {
        this.filename = filename;
        this.length = length;
        this.id = id;
        this.timestamp = timestamp;
        this.username = username;
    }

    public FileTransfer(String filename, long length, Date timestamp)
    {
        this.filename = filename;
        this.length = length;
        this.timestamp = timestamp;
    }

    public FileTransfer(File file)
    {
        this.file = file;
        this.filename = file.getName();
        this.length = file.length();
        timestamp = new Date();//current time

    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public long getLength()
    {
        return length;
    }

    public void setLength(long length)
    {
        this.length = length;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
