package me.txmc.protocolapi;

import io.netty.channel.ChannelPipeline;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {
    private final JavaPlugin plugin;
    private final PacketEventDispatcher dispatcher;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        EntityPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
        ChannelPipeline pipeline = player.playerConnection.networkManager.channel.pipeline();
        if (pipeline.get(String.format("packet_listener%s", plugin.getName())) != null) return;
        pipeline.addBefore("packet_handler", String.format("packet_listener%s", plugin.getName()), new ChannelInjector(dispatcher, plugin, event.getPlayer()));
    }
}
