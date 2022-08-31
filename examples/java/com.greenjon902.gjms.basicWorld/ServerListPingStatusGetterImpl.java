package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.common.ServerListPingStatusGetter;
import com.greenjon902.gjms.common.World;

public class ServerListPingStatusGetterImpl implements ServerListPingStatusGetter {
    @Override
    public String getVersionName() {
        return "GJMS - Example - Basic World";
    }

    @Override
    public int getProtocolNumber(int clientsProtocolNumber) {
        return clientsProtocolNumber;
    }

    @Override
    public int getMaxPlayers() {
        return 0;
    }

    @Override
    public int getOnlinePlayers() {
        return 0; // TODO: Get a correct value for this
    }

    @Override
    public String getPlayerSample() {
        return "[]"; // TODO: Get a correct value for this
    }

    @Override
    public String getDescription() {
        return "{\"text\": \"A basic world with just a stone platform!\"}";
    }

    @Override
    public boolean getPreviewsChat() {
        return false;
    }

    @Override
    public String getFavicon() {
        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAw0lEQVRoge3a0QqCMBhA4f3RY+/S914Xkx8txDLkbHC+C5m2YMcZRBSttTKzB72AfxlAM4BmAM0AmgE0A2gG0AygGUAzgGYAzQCaATQDaAbQDKAZQDOANn3A89c3LMtyxzq2aq3fT55+BwygTR8Q/lcCZgDNAJoBNANoBtAMoA0UEBERkYM+7qd53M5crw/ydTpit5I87YPPY84caAfK/jYf6Q05c6yAI2+PSWutZ5QLP6vcJBdUzvYhX12fsUE+A5e9AIgbM5a3UzeGAAAAAElFTkSuQmCC";
    }
}