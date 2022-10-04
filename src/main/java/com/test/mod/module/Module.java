package com.test.mod.module;

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
import com.test.mod.Utils.Connection;
import com.test.mod.Utils.Side;
import io.netty.channel.ChannelHandlerContext;
import com.test.mod.settings.ModeSetting;
import java.util.Iterator;
import com.test.mod.settings.EnableSetting;
import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraftforge.common.MinecraftForge;
import com.test.mod.settings.Setting;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class Module
{
    public static final Minecraft mc;
    private String name;
    private int key;
    private ModuleType moduleType;
    private boolean state;
    private ArrayList<Setting> setting;
    
    public Module(final String name, final int key, final ModuleType moduleType, final boolean toggle) {
        super();
        this.name = name;
        this.key = key;
        this.moduleType = moduleType;
        this.setting = new ArrayList<Setting>();
        if (toggle) {
            this.setState(true, true);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public void add(final Setting... settings) {
        for (final Setting setting : settings) {
            this.setting.add(setting);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setState(final boolean state, final boolean send) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (state) {
            MinecraftForge.EVENT_BUS.register((Object)this);
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("module", this.getName());
            jsonObject.addProperty("enable", "true");
            jsonObject.addProperty("send", send);
            final String message = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject);
            this.Send("127.0.0.1", 20660, message);
            this.onEnable();
        }
        else {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("module", this.getName());
            jsonObject.addProperty("enable", "false");
            jsonObject.addProperty("send", send);
            final String message = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject);
            this.Send("127.0.0.1", 20660, message);
            this.onDisable();
        }
    }
    
    public void setKey(final int key) {
        this.key = key;
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("module", this.getName());
        jsonObject.addProperty("key", Keyboard.getKeyName(this.key));
        final String message = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject);
        this.Send("127.0.0.1", 20440, message);
    }
    
    public void send(final Packet packetIn) {
    }
    
    public void toggleModule(final boolean send) {
        this.setState(!this.state, send);
    }
    
    public ModuleType getModuleType() {
        return this.moduleType;
    }
    
    public void setModuleType(final ModuleType moduleType) {
        this.moduleType = moduleType;
    }
    
    public boolean isToggledValue(final String name) {
        for (final Setting setting : this.getSettings()) {
            if (setting instanceof EnableSetting && ((EnableSetting)setting).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isState() {
        return this.state;
    }
    
    public boolean isToggledMode(final String modeName) {
        for (final Setting setting : this.getSettings()) {
            if (setting instanceof ModeSetting) {
                for (final String mode : ((ModeSetting)setting).getModes()) {
                    if (mode.equals(modeName) && ((ModeSetting)setting).getCurrent().equals(mode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void onPacketEvent(final ChannelHandlerContext p_channelRead0_1_, final Packet p_channelRead0_2_) {
    }
    
    public boolean onPacket(final Object packet, final Side side) {
        return true;
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
    
    public void onEnable() {
    }
    
    public ArrayList<Setting> getSettings() {
        return this.setting;
    }
    
    public void onDisable() {
    }
}
