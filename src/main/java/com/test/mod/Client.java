package com.test.mod;

import java.util.Iterator;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import com.test.mod.Utils.Connection;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.lang.reflect.Field;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
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
import com.test.mod.run.RunSocket;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import com.test.mod.event.Event;
import com.test.mod.command.CommandManager;
import com.test.mod.module.ModuleManager;

public enum Client
{
    instance;
    
    private boolean init;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public Event event;
    public static boolean isObfuscate;
    public static boolean isLogin;
    public String username = "cracked";
    public String qq = "114514";

    private Client() {
//        this.username = null;
//        this.qq = null;
    }
    
    static {
        Client.isObfuscate = false;
        Client.isLogin = true;
    }
    
    public void run() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        FMLCommonHandler.instance().bus().register((Object)this);
        Client.isLogin = true;
        Client.instance.moduleManager = new ModuleManager();
        Client.instance.commandManager = new CommandManager();
        Client.instance.event = new Event();
        Client.instance.moduleManager.loadModules();
        Client.instance.setOBF();
        this.Send("127.0.0.1", 20660, "I am Leave Lite", false);
        new RunSocket();
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
    
    public void setOBF() {
        try {
            final Field F = S18PacketEntityTeleport.class.getDeclaredField("field_70165_t");
            Client.isObfuscate = true;
        }
        catch (NoSuchFieldException ex) {
            try {
                final Field F = S18PacketEntityTeleport.class.getDeclaredField("posX");
                Client.isObfuscate = false;
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Tools.nullCheck()) {
            this.init = false;
            return;
        }
        if (!this.init) {
            new Connection();
            this.init = true;
        }
    }
    @SubscribeEvent
    public void onKey(final InputEvent.KeyInputEvent keyInputEvent) {
        final int key = Keyboard.getEventKey();
        if (key == 0) {
            return;
        }
        if (Keyboard.getEventKeyState()) {
            for (final Module module : this.moduleManager.getModules()) {
                if (module.getKey() == key) {
                    module.toggleModule(false);
                }
            }
        }
    }
}
