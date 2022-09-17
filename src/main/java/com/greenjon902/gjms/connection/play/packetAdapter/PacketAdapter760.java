package com.greenjon902.gjms.connection.play.packetAdapter;

import com.greenjon902.gjms.common.RegistryCodec.RegistryCodec;
import com.greenjon902.gjms.connection.ClientboundPacket;
import com.greenjon902.gjms.connection.PacketAdapter;
import com.greenjon902.gjms.connection.ServerboundPacket;
import com.greenjon902.gjms.connection.play.packetAdapter.packet.clientbound.Login;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketAdapter760 extends PacketAdapter {
    @Override
    public ServerboundPacket decodePacket(InputStream inputStream) throws IOException {
        int packetId = decodeFirstVarInt(inputStream);
        throw new RuntimeException("Unknown packet with ID " + packetId);
    }

    @Override
    public byte[] encodePacket(ClientboundPacket packet) {
        byte[] content;

        if (packet instanceof Login login) {
            byte[] entityId = encodeInt(login.entityId);
            byte[] isHardcore = encodeBoolean(login.isHardcore);
            byte[] previousGamemode = encodeGamemode(login.previousGamemode);
            byte[] gamemode = encodeGamemode(login.gamemode);
            byte[] dimensionNames = encodeArray(login.dimensionNames, this::encodeIdentifier);
            byte[] registryCodec = encodeRegistryCodec(login.registryCodec);
            byte[] dimensionType = encodeIdentifier(login.dimensionType);
            byte[] dimensionName = encodeIdentifier(login.dimensionName);
            byte[] hashedSeed = encodeLong(login.hashedSeed);
            byte[] maxPlayers = encodeVarInt(login.maxPlayers);
            byte[] viewDistance = encodeVarInt(login.viewDistance);
            byte[] simulationDistance = encodeVarInt(login.simulationDistance);
            byte[] reducedDebugInfo = encodeBoolean(login.reducedDebugInfo);
            byte[] enableRespawnScreen = encodeBoolean(login.enableRespawnScreen);
            byte[] isDebug = encodeBoolean(login.isDebug);
            byte[] isFlat = encodeBoolean(login.isFlat);
            byte[] deathLocationInfo;
            if (login.hasDeathLocation()) {
                byte[] hasDeathLocation = encodeBoolean(true);
                byte[] deathDimensionName = encodeIdentifier(login.deathDimensionName);
                byte[] deathLocation = encodeLocation(login.deathLocation);
                deathLocationInfo = new byte[hasDeathLocation.length + deathDimensionName.length +
                        deathDimensionName.length];
                System.arraycopy(hasDeathLocation, 0, deathLocationInfo, 0, hasDeathLocation.length);
                System.arraycopy(deathDimensionName, 0, deathLocationInfo, hasDeathLocation.length,
                        deathDimensionName.length);
                System.arraycopy(deathLocation, 0, deathLocationInfo, hasDeathLocation.length +
                        deathDimensionName.length, deathLocation.length);
            } else {
                deathLocationInfo = encodeBoolean(false); // there is no death location info and the first field
                                                                 // is hasDeathLocation so set to false here
            }

            content = new byte[entityId.length + isHardcore.length + previousGamemode.length + gamemode.length +
                    dimensionNames.length + registryCodec.length + dimensionType.length + dimensionName.length +
                    hashedSeed.length + maxPlayers.length + viewDistance.length + simulationDistance.length +
                    reducedDebugInfo.length + enableRespawnScreen.length + isDebug.length + isFlat.length +
                    deathLocationInfo.length];
            int offset = 0;
            System.arraycopy(entityId, 0, content, offset, entityId.length);
            offset += entityId.length;
            System.arraycopy(isHardcore, 0, content, offset, isHardcore.length);
            offset += isHardcore.length;
            System.arraycopy(previousGamemode, 0, content, offset, previousGamemode.length);
            offset += previousGamemode.length;
            System.arraycopy(gamemode, 0, content, offset, gamemode.length);
            offset += gamemode.length;
            System.arraycopy(dimensionNames, 0, content, offset, dimensionNames.length);
            offset += dimensionNames.length;
            System.arraycopy(registryCodec, 0, content, offset, registryCodec.length);
            offset += registryCodec.length;
            System.arraycopy(dimensionType, 0, content, offset, dimensionType.length);
            offset += dimensionType.length;
            System.arraycopy(dimensionName, 0, content, offset, dimensionName.length);
            offset += dimensionName.length;
            System.arraycopy(hashedSeed, 0, content, offset, hashedSeed.length);
            offset += hashedSeed.length;
            System.arraycopy(maxPlayers, 0, content, offset, maxPlayers.length);
            offset += maxPlayers.length;
            System.arraycopy(viewDistance, 0, content, offset, viewDistance.length);
            offset += viewDistance.length;
            System.arraycopy(simulationDistance, 0, content, offset, simulationDistance.length);
            offset += simulationDistance.length;
            System.arraycopy(reducedDebugInfo, 0, content, offset, reducedDebugInfo.length);
            offset += reducedDebugInfo.length;
            System.arraycopy(enableRespawnScreen, 0, content, offset, enableRespawnScreen.length);
            offset += enableRespawnScreen.length;
            System.arraycopy(isDebug, 0, content, offset, isDebug.length);
            offset += isDebug.length;
            System.arraycopy(isFlat, 0, content, offset, isFlat.length);
            offset += isFlat.length;
            System.arraycopy(deathLocationInfo, 0, content, offset, deathLocationInfo.length);

        } else {
            throw new RuntimeException("PacketAdapter760 cannot encode packet of type " +
                    packet.getClass().toString());
        }

        byte[] packetId = encodeVarInt(packet.getPacketId());
        byte[] packetLength = encodeVarInt(packetId.length + content.length);

        byte[] encodedPacket = new byte[packetLength.length + packetId.length + content.length];
        System.arraycopy(packetLength, 0, encodedPacket, 0, packetLength.length);
        System.arraycopy(packetId, 0, encodedPacket, packetLength.length, packetId.length);
        System.arraycopy(content, 0, encodedPacket, packetLength.length + packetId.length, content.length);
        return encodedPacket;
    }
}
