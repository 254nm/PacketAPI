package me.txmc.protocolapi;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public class ChannelInjector extends ChannelDuplexHandler {
    private final PacketEventDispatcher dispatcher;
    private final JavaPlugin plugin;
    private final Player player;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        try {
            PacketEvent.Outgoing outgoing = new PacketEvent.Outgoing((Packet<?>) msg, player);
            dispatcher.dispatch(outgoing);
            if (outgoing.isCancelled()) return;
            super.write(ctx, outgoing.getPacket(), promise);
        } catch (Throwable t) {
            plugin.getLogger().severe("A Packet listener had an exception");
            t.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (!ctx.channel().isOpen()) return;
            PacketEvent.Incoming incoming = new PacketEvent.Incoming((Packet<?>) msg, player);
            dispatcher.dispatch(incoming);
            if (incoming.isCancelled()) return;
            super.channelRead(ctx, incoming.getPacket());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
