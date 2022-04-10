package me.txmc.protocolapi;

import me.txmc.protocolapi.reflection.ClassProcessor;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PacketEventDispatcher {
    private final HashMap<HashSet<Class<? extends Packet<?>>>, PacketListener> listeners;
    private final JavaPlugin plugin;

    public PacketEventDispatcher(JavaPlugin plugin) {
        this.plugin = plugin;
        listeners = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin, this), plugin);
    }

    public void register(PacketListener listener, Class<? extends Packet<?>>[] listeningFor) {
        if (listeners.containsValue(listener)) return;
        if (ClassProcessor.hasAnnotation(listener)) ClassProcessor.process(listener);
        listeners.put(new HashSet<>(Arrays.asList(listeningFor)), listener);
    }

    public void unregister(PacketListener listener) {
        throw new UnsupportedOperationException("Unregistering listeners is unsupported");
    }

    @SuppressWarnings(value = "unchecked")
    protected void dispatch(PacketEvent event) {
        Class<? extends Packet<?>> clazz = (Class<? extends Packet<?>>) event.getPacket().getClass();
        List<PacketListener> pl = listeners.keySet().stream().filter(s -> s.contains(clazz) || s.contains(null)).map(listeners::get).collect(Collectors.toList());
        if (event instanceof PacketEvent.Incoming) {
            pl.forEach(l -> {
                try {
                    l.incoming((PacketEvent.Incoming) event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } else if (event instanceof PacketEvent.Outgoing) {
            pl.forEach(l -> {
                try {
                    l.outgoing((PacketEvent.Outgoing) event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } else throw new IllegalArgumentException("PacketEvent is an abstract class");
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
