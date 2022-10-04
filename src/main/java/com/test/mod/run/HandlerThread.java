package com.test.mod.run;

import com.test.mod.event.Event;
import com.test.mod.command.CommandManager;
import com.test.mod.module.ModuleManager;
import java.io.OutputStream;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.io.InputStream;
import com.test.mod.module.Module;
import com.test.mod.config.configs.ModuleConfig;
import com.test.mod.config.configs.ModeConfig;
import com.test.mod.config.configs.KeyBindConfig;
import com.test.mod.config.configs.IntegerConfig;
import com.test.mod.config.configs.EnableConfig;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import com.test.mod.settings.ModeSetting;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.Setting;
import com.test.mod.Utils.ChatUtils;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import org.lwjgl.input.Keyboard;
import com.test.mod.Client;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class HandlerThread extends Thread
{
    private Socket socket;
    
    public HandlerThread(final Socket socket) {
        super();
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            final InputStream inputStream = this.socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(inputStream, "GBK");
            final BufferedReader br = new BufferedReader(ipsr);
            String s = null;
            while ((s = br.readLine()) != null) {
                final JsonObject jsonObject = (JsonObject)new Gson().fromJson(s, (Class)JsonObject.class);
                if (jsonObject.has("module")) {
                    final String module = jsonObject.get("module").getAsString();
                    final boolean enable = jsonObject.get("enable").getAsBoolean();
                    final Module mod = Client.instance.moduleManager.getModule(module);
                    mod.setState(enable, false);
                }
                if (jsonObject.has("username")) {
                    final String name = jsonObject.get("username").getAsString();
                    final String qq = jsonObject.get("qq").getAsString();
                    Client.instance.username = name;
                    Client.instance.qq = qq;
                }
                if (jsonObject.has("get_key")) {
                    final String module = jsonObject.get("get_key").getAsString();
                    final Module mod = Client.instance.moduleManager.getModule(module);
                    final JsonObject jsonObject_bind = new JsonObject();
                    jsonObject_bind.addProperty("module", mod.getName());
                    jsonObject_bind.addProperty("key", Keyboard.getKeyName(mod.getKey()));
                    final String message = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject_bind);
                    this.Send("127.0.0.1", 20440, message, false);
                }
                if (jsonObject.has("bind_module")) {
                    final String module = jsonObject.get("bind_module").getAsString();
                    final String key = jsonObject.get("key").getAsString();
                    final Module mod = Client.instance.moduleManager.getModule(module);
                    mod.setKey(Keyboard.getKeyIndex(key));
                    ChatUtils.message(module + " key changed to §9" + Keyboard.getKeyName(mod.getKey()));
                }
                if (jsonObject.has("setting_module")) {
                    final String module = jsonObject.get("setting_module").getAsString();
                    final Module mod = Client.instance.moduleManager.getModule(module);
                    for (final Setting setting : mod.getSettings()) {
                        final String settingValue = jsonObject.get(setting.getName()).getAsString();
                        if (setting instanceof EnableSetting) {
                            final boolean setting_enable = Boolean.parseBoolean(settingValue);
                            ((EnableSetting)setting).setEnable(setting_enable);
                        }
                        else if (setting instanceof IntegerSetting) {
                            final double setting_integer = Double.parseDouble(settingValue);
                            ((IntegerSetting)setting).setCurrent(setting_integer);
                        }
                        else {
                            if (!(setting instanceof ModeSetting)) {
                                continue;
                            }
                            ((ModeSetting)setting).setCurrent(settingValue);
                        }
                    }
                }
                if (jsonObject.has("Operation")) {
                    if (jsonObject.get("Operation").getAsString().equals("kick")) {
                        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
                    }
                    if (jsonObject.get("Operation").getAsString().equals("Login")) {
                        final String username = jsonObject.get("Username").getAsString();
                        final String password = jsonObject.get("Password").getAsString();
                        final String hwid = jsonObject.get("HWID").getAsString();
                        final String IP = jsonObject.get("IP").getAsString();
                        final JsonObject jsonObject_Login = new JsonObject();
                        jsonObject_Login.addProperty("type", "LOGIN");
                        jsonObject_Login.addProperty("username", username);
                        jsonObject_Login.addProperty("password", password);
                        jsonObject_Login.addProperty("hwid", hwid);
                        final String message2 = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject_Login);
                        System.out.println(s);
                    }
                }
                if (jsonObject.has("Config")) {
                    if (jsonObject.get("Config").getAsString().equals("save")) {
                        EnableConfig.saveState();
                        IntegerConfig.saveState();
                        KeyBindConfig.saveKey();
                        ModeConfig.saveState();
                        ModuleConfig.saveModules();
                    }
                    else {
                        if (!jsonObject.get("Config").getAsString().equals("load")) {
                            continue;
                        }
                        EnableConfig.loadState();
                        IntegerConfig.loadState();
                        KeyBindConfig.loadKey();
                        ModeConfig.loadState();
                        ModuleConfig.loadModules();
                        for (final Module module2 : Client.instance.moduleManager.getModules()) {
                            final JsonObject jsonObject_mod = new JsonObject();
                            jsonObject_mod.addProperty("module", module2.getName());
                            jsonObject_mod.addProperty("enable", module2.isState());
                            final String message3 = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject_mod);
                            this.Send("127.0.0.1", 20660, message3, false);
                        }
                        for (final Module module2 : Client.instance.moduleManager.getModules()) {
                            final JsonObject jsonObject_setting = new JsonObject();
                            if (!module2.getSettings().isEmpty()) {
                                for (final Setting setting2 : module2.getSettings()) {
                                    jsonObject_setting.addProperty("setting_module", module2.getName());
                                    if (setting2 instanceof EnableSetting) {
                                        jsonObject_setting.addProperty(setting2.getName(), ((EnableSetting)setting2).getEnable());
                                    }
                                    else if (setting2 instanceof IntegerSetting) {
                                        jsonObject_setting.addProperty(setting2.getName(), (Number)((IntegerSetting)setting2).getCurrent());
                                    }
                                    else {
                                        if (!(setting2 instanceof ModeSetting)) {
                                            continue;
                                        }
                                        jsonObject_setting.addProperty(setting2.getName(), ((ModeSetting)setting2).getCurrent());
                                    }
                                }
                                final String message3 = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject_setting);
                                this.Send("127.0.0.1", 20550, message3, false);
                            }
                        }
                        for (final Module module2 : Client.instance.moduleManager.getModules()) {
                            final JsonObject jsonObject_mod = new JsonObject();
                            jsonObject_mod.addProperty("module", module2.getName());
                            jsonObject_mod.addProperty("key", Keyboard.getKeyName(module2.getKey()));
                            final String message3 = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)jsonObject_mod);
                            this.Send("127.0.0.1", 20440, message3, false);
                        }
                        this.Send("127.0.0.1", 20660, "Config Loaded", false);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public String Send(final String IP, final int Port, final String Message, final boolean login) {
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
    
    public void Load(final String IP, final String message) {
        try {
            System.out.println(message);
            final Socket socket = new Socket(IP, 12892);
            final OutputStream ops = socket.getOutputStream();
            final OutputStreamWriter opsw = new OutputStreamWriter(ops);
            final BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(message);
            bw.flush();
            final InputStream ips = socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(ips, "GBK");
            final BufferedReader br = new BufferedReader(ipsr);
            String s = null;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
                final JsonObject jsonObject = (JsonObject)new Gson().fromJson(s, (Class)JsonObject.class);
                if (jsonObject.get("type").getAsString().equals("LOGIN")) {
                    final String return_message = jsonObject.get("message").getAsString();
                    if (return_message.equals("SUCCESS")) {
                        Client.isLogin = true;
                        Client.instance.moduleManager = new ModuleManager();
                        Client.instance.commandManager = new CommandManager();
                        Client.instance.event = new Event();
                        Client.instance.moduleManager.loadModules();
                        Client.instance.setOBF();
                        this.Send("127.0.0.1", 20660, "I am Leave Lite", false);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Login Failed", "Leave_Lite", 0);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Unknown error", "Leave_Lite", 0);
                }
            }
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed Connect to The Server", "Leave_Lite", 0);
        }
    }
}
