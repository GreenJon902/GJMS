package com.greenjon902.gjms;

import com.greenjon902.gjms.common.ConnectionHandler;
import com.greenjon902.gjms.common.FirstWorldGetter;
import com.greenjon902.gjms.common.Player;
import com.greenjon902.gjms.common.ServerListPingStatusGetter;
import com.greenjon902.gjms.connection.NewConnectionHandler;
import com.greenjon902.gjms.connection.prePlay.PrePlayConnectionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * The tools that a lot of tests will require
 */
public class Utils {
    public static byte[] byteArray(int... arr) {
        byte[] byteArray = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            byteArray[i] = (byte) arr[i];
        }
        return byteArray;
    }

    private static Integer port = null;
    /**
     * Make a server on any port if not yet made.
     * @return The port
     */
    public static int makeServer(boolean cracked) {
        if (port == null) {
            ConnectionHandler prePlayConnectionHandler = new PrePlayConnectionHandler(new FirstWorldGetterImpl(),
                    new ServerListPingStatusGetterImpl());
            NewConnectionHandler newConnectionHandler = new NewConnectionHandler(cracked, prePlayConnectionHandler);
            newConnectionHandler.startNewHandler();
            prePlayConnectionHandler.startNewHandler();

            port = newConnectionHandler.getPort();
        }
        return port;
    }

    public static int readVarInt(InputStream inputStream) throws IOException {
        int value = 0;
        int position = 0;
        while (true) {
            byte currentByte = (byte) inputStream.read();
            value |= (currentByte & 0x7F) << position;
            if ((currentByte & 0x80) == 0) break;
            position += 7;
        }
        return value;
    }
}

class FirstWorldGetterImpl implements FirstWorldGetter {
    @Override
    public String getWorld(UUID playerId) {
        return null;
    }
}

/**
 * Random information for testing
 */
class ServerListPingStatusGetterImpl implements ServerListPingStatusGetter {
    @Override
    public String getVersionName() {
        return "GJMS TestUtil Server";
    }

    @Override
    public int getProtocolNumber(int clientsProtocolNumber) {
        return clientsProtocolNumber;
    }

    @Override
    public int getMaxPlayers() {
        return 20;
    }

    @Override
    public int getOnlinePlayers() {
        return 12;
    }

    @Override
    public String getPlayerSample() {
        return "[{\"name\":\"GreenJon\",\"id\":\"86f5d3d8-0d4b-4230-9852-77a40baf39bd\"}," +
                "{\"name\":\"AdminJon_\",\"id\":\"0f549ef4-000b-4a9a-8fd2-2c3e7044ea54\"}," +
                "{\"name\":\"Dream\",\"id\":\"ec70bcaf-702f-4bb8-b48d-276fa52a780c\"}]";
    }

    @Override
    public String getDescription() {
        return "{\"text\": \"Hello World!\"}";
    }

    @Override
    public boolean getPreviewsChat() {
        return false;
    }

    public String getFavicon() {
        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAAACXBIWXMAAA7EAAAOxAGVKw4bAAACyklEQVRoge1avW4UMRD+5nR5hfAYFBRIFLwCBUWQiBQKulAggQQSFCCBBBIUSKFESqRESqQUvAIFDUWKvEV4hZwyFN7zene9Xts7s7t33KfodPHa4/k8Px57j8DAzQyLecLf9dZ0+s+w4pgPN9XXlwCwfyArdRACH9+W378/K77sHYrIpqQQuPvHI+J0p2PUu/cA8OILFnN8e14OfHwsEDIeC9z6K7I0BYz2rz5hAQDYPygm/vEUJ7vYOe0rP82Fft/zrVDWxHuHOHqCs0d4eJ41fgkPgattj6VuX/aaRg8E8Aw3Mf52gTsA7uPXFq4jXfQzXgP4gDdtHXZxAuAnHriN8fLnWKz8PrAhMDaImWO7EgGI7985JENgEwOWEg6M6iIYlEBN755rX8gUdIm2/l6IaA9tCzCziKMHMFAWEnT6uuTUtSFKHgKHgLgpcrTJnCnOCKn6DLeRMbPGYg1nASWsfCmxIZALIhLJrRsLjI2oUsJr64mkr24CbZ6aVOTolRKxxVxTURGd+pd63RuZRjnZRj5jlnFOZAZWXcsnY7G6s5ARp+fEWJZJZqLpFnMxyHCh0QhIBVU9iAnVczcqblp0dt3Jp0f4uWxWqARxTXvbYmmgGQmmpQzH8HN5lC7U1N59VIRya4+OENdLAQWBgPax6CoulTjEBrEAQyNHeltMyEJmzjYehHFqu2ntAxn4zwj09BKNujCWADu+r1gVpWMtXKhMkbprq3JN3TgPuBx8c/HEXKhKoKadgt8TCddFDQu40iWXmhrSZRA8UjrTcV8yWq43ZBZSqTXWIo2uNDYEEiF+tlwLC/BIZxERTMgC9p0NLWHb7afb07QUGxmDpU69AQQCwH1/bn6gYK807b/upx04IQsg7gbWcLA9SwI9I0H1fU2tCDc3wRUXipHR82YnzNAqhC471H524XnB0XY9GiLQfrVYfS6Pf72WrKmUkxxkAAAAAElFTkSuQmCC";
    }
}