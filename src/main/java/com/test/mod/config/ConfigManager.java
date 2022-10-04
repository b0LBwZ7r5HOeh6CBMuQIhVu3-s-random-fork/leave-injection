package com.test.mod.config;

import com.test.mod.Utils.Tools;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class ConfigManager
{
    public String path;
    public String filename;
    
    public ConfigManager(final String path, final String filename) {
        super();
        this.path = path;
        this.filename = filename;
        this.createFile();
    }
    
    public void clear() {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.path, this.filename)));
            bw.write("");
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void write(final String[] text) {
        if (text == null || text.length == 0 || text[0].trim() == "") {
            return;
        }
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.path, this.filename), true));
            for (final String line : text) {
                bw.write(line);
                bw.write("\r\n");
            }
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void write(final String text) {
        this.write(new String[] { text });
    }
    
    public final ArrayList<String> read() {
        final ArrayList list = new ArrayList();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(this.path, this.filename).getAbsolutePath()))));
            while (true) {
                final String text = br.readLine();
                if (text == null) {
                    break;
                }
                list.add(text.trim());
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<String>)list;
    }
    
    public void createFile() {
        try {
            final File n = new File(this.path + "\\" + this.filename);
            final File ConfigDir = new File(Tools.getConfigPath());
            if (!ConfigDir.exists()) {
                ConfigDir.mkdir();
            }
            final File FontDir = new File(Tools.getFontPath());
            if (!FontDir.exists()) {
                FontDir.mkdir();
            }
            if (!n.exists()) {
                n.createNewFile();
            }
        }
        catch (Exception ex) {}
    }
}
