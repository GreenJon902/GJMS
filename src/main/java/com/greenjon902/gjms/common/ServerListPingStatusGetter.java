package com.greenjon902.gjms.common;

public interface ServerListPingStatusGetter {
    String getVersionName();
    int getProtocolNumber(int clientsProtocolNumber);
    int getMaxPlayers();
    int getOnlinePlayers();
    String getPlayerSample();
    String getDescription();
    boolean getPreviewsChat();
    String getFavicon();
}
