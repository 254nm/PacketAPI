package me.txmc.protocolapiexample;

import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapiexample.listeners.ChatListener;
import net.minecraft.server.v1_12_R1.PacketPlayInChat;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProtocolAPIExample extends JavaPlugin {
    @Override
    public void onEnable() {
        PacketEventDispatcher dispatcher = new PacketEventDispatcher(this); //This only needs to be constructed once
        dispatcher.register(new ChatListener(), PacketPlayInChat.class); //Register a listener with a list of packets that it wants to receive events for
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
