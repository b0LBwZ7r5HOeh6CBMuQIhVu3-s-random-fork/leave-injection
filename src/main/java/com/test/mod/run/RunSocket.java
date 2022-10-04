package com.test.mod.run;

import java.io.InputStream;
import java.io.OutputStream;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class RunSocket extends Thread
{
    public RunSocket() {
        super();
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        try {
            final ServerSocket serverSocket = new ServerSocket(18899);
            this.Send("127.0.0.1", 20660, "Connect success");
            while (true) {
                final Socket client = serverSocket.accept();
                new HandlerThread(client).run();
            }
        }
        catch (Exception var2) {
            var2.printStackTrace();
        }
    }
    
    public String Send(final String IP, final int Port, final String Message) {
        try {
            final Socket socket = new Socket(IP, Port);
            final OutputStream ops = socket.getOutputStream();
            final OutputStreamWriter opsw = new OutputStreamWriter(ops, "GBK");
            final BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            final InputStream ips = socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(ips, "GBK");
            final BufferedReader br = new BufferedReader(ipsr);
            final String s = null;
            socket.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed Connect to The Server", "LeaveOld", 0);
        }
        return null;
    }
}
