package com.greenjon902.gjms.connection.prePlay.packetAdapter.status.packet.clientbound;

import com.greenjon902.gjms.common.ServerListPingStatusGetter;
import com.greenjon902.gjms.connection.ClientboundPacket;

public class StatusResponse extends ClientboundPacket {
    public final String versionName;
    public final int protocolNumber;

    public final int maxPlayers;
    public final int onlinePlayers;
    public final String playerSample; // TODO: Update to support actual user classes or something that can support UUIDs

    public final String description; // TODO: Update to use actual chat components
    public final String favicon; // TODO: Update to use an actual image
    public final boolean previewsChat;

    public StatusResponse(String versionName, int protocolNumber, int maxPlayers, int onlinePlayers, String playerSample, String description, String favicon, boolean previewsChat) {
        this.versionName = versionName;
        this.protocolNumber = protocolNumber;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.playerSample = playerSample;
        this.description = description;
        this.favicon = favicon;
        this.previewsChat = previewsChat;
    }

    public StatusResponse(String versionName, int protocolNumber, int maxPlayers, int onlinePlayers, String playerSample, String description, boolean previewsChat) {
        this.versionName = versionName;
        this.protocolNumber = protocolNumber;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.playerSample = playerSample;
        this.description = description;
        this.favicon = null;
        this.previewsChat = previewsChat;
    }

    public StatusResponse(ServerListPingStatusGetter serverListPingStatusGetter, int clientsProtocolNumber) {
        this.versionName = serverListPingStatusGetter.getVersionName();
        this.protocolNumber = serverListPingStatusGetter.getProtocolNumber(clientsProtocolNumber);
        this.maxPlayers = serverListPingStatusGetter.getMaxPlayers();
        this.onlinePlayers = serverListPingStatusGetter.getOnlinePlayers();
        this.playerSample = serverListPingStatusGetter.getPlayerSample();
        this.description = serverListPingStatusGetter.getDescription();
        this.favicon = serverListPingStatusGetter.getFavicon();
        this.previewsChat = serverListPingStatusGetter.getPreviewsChat();
    }

    @Override
    public int getPacketId() {
        return 0;
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "versionName='" + versionName + '\'' +
                ", protocolNumber=" + protocolNumber +
                ", maxPlayers=" + maxPlayers +
                ", onlinePlayers=" + onlinePlayers +
                ", playerSample='" + playerSample + '\'' +
                ", description='" + description + '\'' +
                ", favicon='" + favicon + '\'' +
                ", previewsChat=" + previewsChat +
                '}';
    }
}
