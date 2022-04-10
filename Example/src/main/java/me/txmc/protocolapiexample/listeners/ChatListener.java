package me.txmc.protocolapiexample.listeners;

import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.PacketPlayInChat;

import java.lang.reflect.Field;

/**
 * @warning This code isn't running on bukkit's main thread, so you can run into unexpected exceptions while trying to interact with players or anything on the main thread.
 * To fix these errors you can do something like Bukkit.getScheduler().runTask(<instance>, () -> theCodeYouWannaRun()); to run on the main thread
 */
public class ChatListener implements PacketListener {

    @GetField(clazz = PacketPlayInChat.class, name = "a") //The api will automatically set this field
    private Field messageF;

    @Override
    public void incoming(PacketEvent.Incoming incoming) throws Throwable {
        PacketPlayInChat packet = (PacketPlayInChat) incoming.getPacket(); //This is a safe cast because we said we only wanna receive events about this packet when we registered the listener
        String originalMessage = (String) messageF.get(packet); //Get the original message
        messageF.set(packet, originalMessage.concat(" | This message was modified by 254n_m's packet api!")); //Set the message to a modified version of the message
    }

    @Override
    public void outgoing(PacketEvent.Outgoing outgoing) throws Throwable {

    }
}
