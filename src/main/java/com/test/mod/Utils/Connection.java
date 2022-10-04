package com.test.mod.Utils;

import com.test.mod.Client;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import net.minecraft.client.Minecraft;
import io.netty.channel.ChannelDuplexHandler;

public class Connection extends ChannelDuplexHandler
{
    public Connection() {
        super();
        try {
            final ChannelPipeline pipeline = Minecraft.getMinecraft().getNetHandler().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", (ChannelHandler)this);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        if (!Client.instance.event.onPacket(packet, Side.OUT)) {
            return;
        }
        super.write(ctx, packet, promise);
    }
    
    public void channelRead(final ChannelHandlerContext ctx, final Object packet) throws Exception {
        if (!Client.instance.event.onPacket(packet, Side.IN)) {
            return;
        }
        super.channelRead(ctx, packet);
    }
}
