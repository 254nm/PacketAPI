package me.txmc.protocolapi;

import me.txmc.protocolapi.reflection.ClassProcessor;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class PacketEventDispatcher {
    private final LinkedHashMap<PacketListener, HashSet<Class<? extends Packet<?>>>> listeners;
    private final JavaPlugin plugin;

    public PacketEventDispatcher(JavaPlugin plugin) {
        this.plugin = plugin;
        listeners = new LinkedHashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin, this), plugin);
    }

    @SafeVarargs
    public final void register(PacketListener listener, Class<? extends Packet<?>>... listeningFor) {
        if (listeners.containsKey(listener)) return;
        if (ClassProcessor.hasAnnotation(listener)) ClassProcessor.process(listener);
        listeners.put(listener, new HashSet<>(Arrays.asList(listeningFor)));
    }

    public void unregister(PacketListener listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings(value = "unchecked")
    protected void dispatch(PacketEvent event) {
        Class<? extends Packet<?>> clazz = (Class<? extends Packet<?>>) event.getPacket().getClass();
        List<PacketListener> pl = listeners.entrySet().stream().filter(e -> e.getValue().contains(clazz) || e.getValue().contains(null)).map(Map.Entry::getKey).collect(Collectors.toList());
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
}
