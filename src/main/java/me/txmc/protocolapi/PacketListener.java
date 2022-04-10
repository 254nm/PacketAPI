package me.txmc.protocolapi;

public interface PacketListener {
    void incoming(PacketEvent.Incoming event) throws Throwable;

    void outgoing(PacketEvent.Outgoing event) throws Throwable;
}
