package com.greenjon902.gjms;

import com.greenjon902.gjms.socket.SocketManager;

public class GJMS {
    public static void main(String[] args) {
        System.out.println("GJMS Starting...");
        System.out.println("Version is 1.0-SNAPSHOT");

        SocketManager socketManager = new SocketManager(25565);
        socketManager.openSocket();
    }
}
