package com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound;

import com.greenjon902.gjms.connection.ClientboundPacket;

import java.util.Arrays;
import java.util.UUID;

public class LoginSuccess extends ClientboundPacket {
    public final UUID uuid;
    public final String username;
    public final Property[] properties;

    public static class Property {
        public final String name;
        public final String value;
        public final String signature;

        public Property(String name, String value) {
            this(name, value, null);
        }

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public boolean isSigned() {
            return (signature != null);
        }

        @Override
        public String toString() {
            if (isSigned()) {
                return "Property{" +
                        "name=" + name + 
                        ", value=" + value +
                        ", signature=" + signature +
                        '}';
            } else {
                return "Property{" + 
                        "name=" + name + 
                        ", value=" + value +
                        '}';
            }
        }
    }

    public LoginSuccess(UUID uuid, String username, Property[] properties) {
         this.uuid = uuid;
         this.username = username;
         this.properties = properties;
    }

    @Override
    public int getPacketId() {
        return 2;
    }

    @Override
    public String toString() {
        return "LoginSuccess{" +
                "uuid=" + uuid +
                ", username=" + username +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
