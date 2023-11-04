package com.example.meitu2.utils;

import org.jitsi.service.neomedia.RawPacket;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class udpSendAllUtils {

    static HashMap<String,String> map = new HashMap<>();

    public static void sendAll(RawPacket rawPacket) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();
        for (Map.Entry entry: map.entrySet()
             ) {
            DatagramPacket packet = new DatagramPacket(rawPacket.getBuffer(),rawPacket.getLength(), InetAddress.getByName((String)entry.getKey()),(Integer) entry.getValue());
            udpSocket.send(packet);
        }
    }
}
