package com.greenjon902.gjms.connection;

import com.greenjon902.gjms.common.Connection;
import com.greenjon902.gjms.common.Gamemode;
import com.greenjon902.gjms.common.Identifier;
import com.greenjon902.gjms.common.Location;
import com.greenjon902.gjms.common.RegistryCodec.*;
import com.greenjon902.gjms.connection.prePlay.packetAdapter.login.packet.clientbound.LoginSuccess;
import me.nullicorn.nedit.NBTWriter;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import me.nullicorn.nedit.type.TagType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

/**
 * Contains the base functionality any {@link PacketAdapter} will need. Any class that extends this class will be used
 * to convert bytes from a {@link Connection} to a {@link ServerboundPacket} and be able to convert a
 * {@link ServerboundPacket} to bytes and send it through a {@link Connection}.<br>
 * The reason for this is that a game will only need to understand one packet system, but it will be able to communicate
 * with legacy and future clients without any extra development as all conversions will be handled by the
 * {@link PacketAdapter}.
 */
public abstract class PacketAdapter {
    private static final int SEGMENT_BITS = 0x7F; // 01111111
    private static final int CONTINUE_BIT = 0x80; // 10000000
    private static final int BYTE_BITS = 0xFF; // 11111111
    private static final int BOOL_TRUE_BITS = 0x01; // 00000001

    /**
     * Reads the first variable length integer from the players incoming packets.
     * VarInts are stored in a way that each byte has 7 bits of data which is prefixed by a bit that shows whether
     * to continue or not, if it is a 0 then the varInt is over, if it is a 1 then the varInt has another byte of data.
     * VarInts can store up to 5 bytes of data.
     *
     * @param inputStream The stream varInt is coming from
     * @return The integer that was decoded
     * @throws IOException If an I/O error occurs
     */
    public int decodeFirstVarInt(@NotNull InputStream inputStream) throws IOException {
        int value = 0; // The number that is being decoded
        int position = 0; // The position of value that is currently being edited

        while (true) {
            byte currentByte = (byte) inputStream.read();

            value |= (currentByte & SEGMENT_BITS) << position; // adds the new byte to the value

            if ((currentByte & CONTINUE_BIT) == 0) break; // is the number finished?
            position += 7; // because bytes in varInts store 7 bits of data
            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    /**
     * Reads the first boolean from the connection.
     *
     * @param inputStream The stream boolean is coming from
     * @return The boolean that was decoded
     * @throws IOException If an I/O error occurs
     */
    public boolean decodeFirstBoolean(@NotNull InputStream inputStream) throws IOException {
        return (inputStream.read() & BOOL_TRUE_BITS) == 1;
    }

    /**
     * Reads the byte array from the connection, it will also read the first varInt as that signifies the length of the
     * array.
     *
     * @param inputStream The stream where the byte array is coming from
     * @return The byte[] that was decoded
     * @throws IOException If an I/O error occurs
     */
    public byte[] decodeFirstByteArray(@NotNull InputStream inputStream) throws IOException {
        int length = decodeFirstVarInt(inputStream);
        byte[] content = new byte[length];
        inputStream.read(content);
        return content;
    }

    /**
     * Encodes an integer as a varInt in a byte array.
     * VarInts are stored in a way that each byte has 7 bits of data which is prefixed by a bit that shows whether
     * to continue or not, if it is a 0 then the varInt is over, if it is a 1 then the varInt has another byte of data.
     * VarInts can store up to 5 bytes of data.
     *
     * @param value The int to be encoded
     * @return The byte[] of the varInt
     */
    public byte[] encodeVarInt(int value) {
        byte[] encoded = new byte[5]; // maximum it can hold
        int i = 0;
        while (true){
            if ((value & ~SEGMENT_BITS) == 0) {
                encoded[i] = (byte) value;
                break;
            }
            encoded[i] = (byte) ((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
            i++;
        }

        byte[] croppedEncoded = new byte[i+1];
        System.arraycopy(encoded, 0, croppedEncoded, 0, i+1); // remove the empty values from the array
        return croppedEncoded;
    }

    /**
     * Reads the first string from the players incoming packets.
     * Strings are transmitted in UTF-8 and are prefixed by a varInt (see {@link #decodeFirstVarInt(InputStream)}).
     *
     * @param inputStream The connection where the string is coming from
     * @return The string that was decoded
     * @throws IOException If an I/O error occurs
     */
    @Contract("_ -> new")
    public @NotNull String decodeFirstString(@NotNull InputStream inputStream) throws IOException {
        int length = decodeFirstVarInt(inputStream);
        byte[] bytes = new byte[length];
        inputStream.read(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Gets the first {@link Short} from a connection.
     *
     * @param inputStream The short where the packet is coming from
     * @return The long that was decoded
     * @throws IOException If an I/O error occurs
     */
    public int decodeFirstUnsignedShort(@NotNull InputStream inputStream) throws IOException {
        int value = 0;
        value += inputStream.read() << 8;
        value += inputStream.read();
        return value;
    }

    /**
     * Gets the first {@link Long} from a connection.
     *
     * @param inputStream The stream where the long is coming from
     * @return The long that was decoded
     * @throws IOException If an I/O error occurs
     */
    public long decodeFirstLong(@NotNull InputStream inputStream) throws IOException {
        long value = 0;
        for (int i=0;i<8;i++) {
            value <<= 8;
            value |= inputStream.read() & BYTE_BITS;
        }
        return value;
    }

    /**
     * Encodes a long by turning it into bytes.
     *
     * @param value The long to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeLong(long value) {
        return new byte[] {
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    /**
     * Encodes an int by turning it into bytes.
     *
     * @param value The int to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeInt(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    /**
     * Encodes a byte[] by prefixing it with its length.
     *
     * @param byteArray The bytes to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeBytes(byte[] byteArray) {
        byte[] length = encodeVarInt(byteArray.length);
        byte[] bytes = new byte[byteArray.length + length.length];
        System.arraycopy(length, 0, bytes, 0, length.length);
        System.arraycopy(byteArray, 0, bytes, length.length, byteArray.length);
        return bytes;
    }

    /**
     * Encodes a string by turning it into bytes and prefixing it with its length.
     *
     * @param string The string to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeString(String string) {
        return encodeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Encodes a boolean by  turning it into bytes.
     *
     * @param value The boolean to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeBoolean(boolean value) {
        return new byte[] {(byte) (value ? 0x01 : 0x00)};
    }

    /**
     * Encodes a UUID by turning it into 16 bytes / two unsigned longs.
     *
     * @param uuid The uuid to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeUUID(UUID uuid) {
        byte[] mostSignificant = encodeLong(uuid.getMostSignificantBits());
        byte[] leastSignificant = encodeLong(uuid.getLeastSignificantBits());

        byte[] bytes = new byte[16];
        System.arraycopy(mostSignificant, 0, bytes, 0, 8);
        System.arraycopy(leastSignificant, 0, bytes, 8, 8);

        return bytes;
    }

    /**
     * Encodes a {@link LoginSuccess.Property}.
     *
     * @param property The property to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeProperty(LoginSuccess.Property property) {
        byte[] name = encodeString(property.name);
        byte[] value = encodeString(property.value);
        byte[] isSigned = encodeBoolean(property.isSigned());
        byte[] signature; // 0 length if not signed

        if (property.isSigned()) {
            signature = encodeString(property.signature);
        } else {
            signature = new byte[0];
        }

        byte[] bytes = new byte[name.length + value.length + isSigned.length + signature.length];
        System.arraycopy(name, 0, bytes, 0, name.length);
        System.arraycopy(value, 0, bytes, name.length, value.length);
        System.arraycopy(isSigned, 0, bytes, name.length + value.length, isSigned.length);
        System.arraycopy(signature, 0, bytes, name.length + value.length + isSigned.length,
                signature.length);

        return bytes;
    }

    /**
     * Encodes an array of a type with an encoder. It is prefixed by the number of items in the array
     *
     * @param items The items to be encoded
     * @param encoder The function to encode the items
     * @return The  bytes that were got
     * @param <T> The type of the item that's being encoded
     */
    public <T> byte[] encodeArray(T[] items, Function<T, byte[]> encoder) {
        // We don't know what the full length is yet, so we can't make the final array
        byte[][] encodedIndividualItems = new byte[items.length][];
        int totalLength = 0;
        for (int i=0; i < items.length; i++) {
            byte[] encodedItem = encoder.apply(items[i]);
            encodedIndividualItems[i] = encodedItem;
            totalLength += encodedItem.length;
        }

        byte[] encodedLength = encodeVarInt(items.length);
        byte[] encodedItems = new byte[encodedLength.length + totalLength];
        System.arraycopy(encodedLength, 0, encodedItems, 0, encodedLength.length);

        int offset = encodedLength.length;
        for (byte[] encodedItem : encodedIndividualItems) {
            System.arraycopy(encodedItem, 0, encodedItems, offset, encodedItem.length);
            offset += encodedItem.length;
        }

        return encodedItems;
    }

    /**
     * Reads the next packet bytes into a byte[], see {@link #decodeFirstByteArray(InputStream)}
     *
     * @param inputStream The connection where the packet is coming from
     * @return The packet that has been got
     * @throws IOException If an I/O error occurs
     */
    public byte[] readNextPacketFrom(InputStream inputStream) throws IOException {
        return decodeFirstByteArray(inputStream);
    }

    /**
     * Decodes the first packet from an input stream, the stream should only contain one packet - use
     * {@link #readNextPacketFrom(InputStream)}.
     *
     * @param inputStream The stream where the packet is coming from
     * @return The packet that was decoded
     * @throws IOException If an I/O error occurs
     */
    public abstract ServerboundPacket decodePacket(InputStream inputStream) throws IOException;

    /**
     * Encodes a {@link ClientboundPacket} to a byte[].
     *
     * @param clientboundPacket The packet to be encoded
     * @return The encoded packet as a byte[]
     */
    public abstract byte[] encodePacket(ClientboundPacket clientboundPacket);

    /**
     * Encodes a {@link Identifier} as if it were a string.
     *
     * @param identifier The identifier to be encoded
     * @return The bytes that were got
     */
    public byte[] encodeIdentifier(Identifier identifier) {
        return encodeString(identifier.build());
    }

    /**
     * Encodes a {@link Gamemode}.
     *
     * @param gamemode The gamemode
     * @return The bytes that were got
     */
    public byte[] encodeGamemode(Gamemode gamemode) {
        return new byte[]{
                switch (gamemode) {
                    case SURVIVAL -> 0;
                    case CREATIVE -> 1;
                    case ADVENTURE -> 2;
                    case SPECTATOR -> 3;
                }
        };
    }

    /**
     * Encodes a {@link Location} as a long where the first 26 bits are x, the next 26 bits are z, and the next & last
     * 12 bits are y.
     *
     * @param location The location
     * @return The bytes that were got
     */
    public byte[] encodeLocation(Location location) {
        return encodeLong(
                ((long) (location.getX() & 0x3FFFFFF) << 38) |
                ((long) (location.getZ() & 0x3FFFFFF) << 12) |
                (location.getY() & 0xFFF));
    }

    /**
     * Encodes a {@link RegistryCodec} as if it were NBT data.
     *
     * @param registryCodec The registry codec
     * @return The bytes that were got
     */
    public byte[] encodeRegistryCodec(RegistryCodec registryCodec) {

        NBTCompound nbtDimensionTypeRegistry = new NBTCompound();
        nbtDimensionTypeRegistry.put("type", "minecraft:dimension_type");

        NBTList nbtDimensionTypeRegistryEntries = new NBTList(TagType.COMPOUND);
        for (int i=0; i<registryCodec.getDimensionTypes().length; i++) {
            NBTCompound nbtDimensionTypeRegistryEntry = new NBTCompound();
            nbtDimensionTypeRegistryEntry.put("name", registryCodec.getDimensionTypes()[i].getName());
            nbtDimensionTypeRegistryEntry.put("id", i);

            DimensionTypeRegistryEntryElement dimensionTypeRegistryEntryElement =
                    registryCodec.getDimensionTypes()[i].getElement();
            NBTCompound nbtDimensionTypeRegistryEntryElement = new NBTCompound();
            nbtDimensionTypeRegistryEntryElement.put("piglin_safe",
                    dimensionTypeRegistryEntryElement.isPiglinSafe());
            nbtDimensionTypeRegistryEntryElement.put("has_raids", dimensionTypeRegistryEntryElement.hasRaids());
            nbtDimensionTypeRegistryEntryElement.put("monster_spawn_light_level",
                    dimensionTypeRegistryEntryElement.getMonsterSpawnLightLevel());
            nbtDimensionTypeRegistryEntryElement.put("monster_spawn_block_light_limit",
                    dimensionTypeRegistryEntryElement.getMonsterSpawnBlockLightLimit());
            nbtDimensionTypeRegistryEntryElement.put("natural", dimensionTypeRegistryEntryElement.isNatural());
            nbtDimensionTypeRegistryEntryElement.put("ambient_light",
                    dimensionTypeRegistryEntryElement.hasAmbientLight());
            if (dimensionTypeRegistryEntryElement.hasFixedTime()) {
                nbtDimensionTypeRegistryEntryElement.put("fixed_time",
                        dimensionTypeRegistryEntryElement.getFixedTime());
            }
            nbtDimensionTypeRegistryEntryElement.put("infiniburn",
                    dimensionTypeRegistryEntryElement.getInfiniteBurnTag());
            nbtDimensionTypeRegistryEntryElement.put("respawn_anchor_works",
                    dimensionTypeRegistryEntryElement.doRespawnAnchorsWork());
            nbtDimensionTypeRegistryEntryElement.put("has_skylight",
                    dimensionTypeRegistryEntryElement.hasSkylight());
            nbtDimensionTypeRegistryEntryElement.put("bed_works", dimensionTypeRegistryEntryElement.bedWorks());
            nbtDimensionTypeRegistryEntryElement.put("effects", dimensionTypeRegistryEntryElement.getEffects());
            nbtDimensionTypeRegistryEntryElement.put("min_y", dimensionTypeRegistryEntryElement.getMinY());
            nbtDimensionTypeRegistryEntryElement.put("height",
                    dimensionTypeRegistryEntryElement.getWorldHeight());
            nbtDimensionTypeRegistryEntryElement.put("logical_height",
                    dimensionTypeRegistryEntryElement.getLogicalHeight());
            nbtDimensionTypeRegistryEntryElement.put("coordinate_scale",
                    dimensionTypeRegistryEntryElement.getCoordinateScale());
            nbtDimensionTypeRegistryEntryElement.put("ultrawarm",
                    dimensionTypeRegistryEntryElement.isUltrawarm());
            nbtDimensionTypeRegistryEntryElement.put("has_ceiling",
                    dimensionTypeRegistryEntryElement.hasCeiling());

            nbtDimensionTypeRegistryEntry.put("element", nbtDimensionTypeRegistryEntryElement);
            nbtDimensionTypeRegistryEntries.add(nbtDimensionTypeRegistryEntry);
        }
        nbtDimensionTypeRegistry.put("value", nbtDimensionTypeRegistryEntries);


        NBTCompound nbtBiomeRegistry = new NBTCompound();
        nbtBiomeRegistry.put("type", "minecraft:worldgen/biome");
        NBTList nbtBiomeRegistryEntries = new NBTList(TagType.COMPOUND);
        for (int i=0; i<registryCodec.getBiomes().length; i++) {
            NBTCompound nbtBiomeRegistryEntry = new NBTCompound();
            nbtBiomeRegistryEntry.put("name", registryCodec.getDimensionTypes()[i].getName());
            nbtBiomeRegistryEntry.put("id", i);

            BiomeRegistryEntryElement biomeRegistryEntryElement =
                    registryCodec.getBiomes()[i].getElement();
            NBTCompound nbtBiomeRegistryEntryElement = new NBTCompound();
            nbtBiomeRegistryEntryElement.put("precipitation",
                    biomeRegistryEntryElement.getPrecipitationType().getName());
            if (biomeRegistryEntryElement.hasDepthFactor()) {
                nbtBiomeRegistryEntryElement.put("depth", biomeRegistryEntryElement.getDepthFactor());
            }
            nbtBiomeRegistryEntryElement.put("temperature", biomeRegistryEntryElement.getTemperature());
            if (biomeRegistryEntryElement.hasScale()) {
                nbtBiomeRegistryEntryElement.put("scale", biomeRegistryEntryElement.getScale());
            }
            nbtBiomeRegistryEntryElement.put("downfall", biomeRegistryEntryElement.getDownfall());
            if (biomeRegistryEntryElement.hasCategory()) {
                nbtBiomeRegistryEntryElement.put("category", biomeRegistryEntryElement.getCategory());
            }
            if (biomeRegistryEntryElement.hasTemperatureModifier()) {
                nbtBiomeRegistryEntryElement.put("temperature_modifier",
                        biomeRegistryEntryElement.getTemperatureModifier());
            }

            NBTList nbtBiomeRegistryEntryElementEffects = new NBTList(TagType.COMPOUND);
            for (int i2=0; i2<biomeRegistryEntryElement.getEffects().length; i2++) {
                BiomeRegistryEntryElementEffect biomeRegistryEntryElementEffect =
                        biomeRegistryEntryElement.getEffects()[i2];
                NBTCompound nbtBiomeRegistryEntryElementEffect = new NBTCompound();

                nbtBiomeRegistryEntryElementEffect.put("sky_color",
                        biomeRegistryEntryElementEffect.getSkyColor());
                nbtBiomeRegistryEntryElementEffect.put("water_fog_color",
                        biomeRegistryEntryElementEffect.getWaterFogColor());
                nbtBiomeRegistryEntryElementEffect.put("fog_color",
                        biomeRegistryEntryElementEffect.getFogColor());
                nbtBiomeRegistryEntryElementEffect.put("water_color",
                        biomeRegistryEntryElementEffect.getWaterColor());
                if (biomeRegistryEntryElementEffect.hasFoliageColor()) {
                    nbtBiomeRegistryEntryElementEffect.put("foliage_color",
                            biomeRegistryEntryElementEffect.getFoliageColor());
                }
                if (biomeRegistryEntryElementEffect.hasGrassColor()) {
                    nbtBiomeRegistryEntryElementEffect.put("grass_color",
                            biomeRegistryEntryElementEffect.getGrassColor());
                }
                if (biomeRegistryEntryElementEffect.hasGrassColorModifier()) {
                    nbtBiomeRegistryEntryElementEffect.put("grass_color_modifier",
                            biomeRegistryEntryElementEffect.getGrassColorModifier());
                }

                if (biomeRegistryEntryElementEffect.hasMusic()) {
                    BiomeRegistryEntryElementEffectMusic biomeRegistryEntryElementEffectMusic =
                            biomeRegistryEntryElementEffect.getMusic();
                    NBTCompound nbtBiomeRegistryEntryElementEffectMusic = new NBTCompound();
                    nbtBiomeRegistryEntryElementEffectMusic.put("replace_current_music",
                            (byte) (biomeRegistryEntryElementEffectMusic.replacesCurrentMusic() ? 1 : 0));
                    nbtBiomeRegistryEntryElementEffectMusic.put("sound",
                            biomeRegistryEntryElementEffectMusic.getSound());
                    nbtBiomeRegistryEntryElementEffectMusic.put("max_delay",
                            biomeRegistryEntryElementEffectMusic.getMaxDelay());
                    nbtBiomeRegistryEntryElementEffectMusic.put("min_delay",
                            biomeRegistryEntryElementEffectMusic.getMinDelay());

                    nbtBiomeRegistryEntryElementEffect.put("music", nbtBiomeRegistryEntryElementEffectMusic);
                }
                if (biomeRegistryEntryElementEffect.hasAmbientSound()) {
                    nbtBiomeRegistryEntryElementEffect.put("ambient_sound",
                            biomeRegistryEntryElementEffect.getAmbientSound());
                }
                if (biomeRegistryEntryElementEffect.hasAdditionsSound()) {
                    BiomeRegistryEntryElementEffectAdditionsSound biomeRegistryEntryElementEffectAdditionsSound =
                            biomeRegistryEntryElementEffect.getAdditionsSound();
                    NBTCompound nbtBiomeRegistryEntryElementEffectAdditionsSound = new NBTCompound();
                    nbtBiomeRegistryEntryElementEffectAdditionsSound.put("sound",
                            biomeRegistryEntryElementEffectAdditionsSound.getSound());
                    nbtBiomeRegistryEntryElementEffectAdditionsSound.put("tick_chance",
                            biomeRegistryEntryElementEffectAdditionsSound.getTickChance());

                    nbtBiomeRegistryEntryElementEffect.put("additions_sound",
                            nbtBiomeRegistryEntryElementEffectAdditionsSound);
                }
                if (biomeRegistryEntryElementEffect.hasMoodSound()) {
                    BiomeRegistryEntryElementEffectMoodSound biomeRegistryEntryElementEffectMoodSound =
                            biomeRegistryEntryElementEffect.getMoodSound();
                    NBTCompound nbtBiomeRegistryEntryElementEffectMoodSound = new NBTCompound();
                    nbtBiomeRegistryEntryElementEffectMoodSound.put("sound",
                            biomeRegistryEntryElementEffectMoodSound.getSound());
                    nbtBiomeRegistryEntryElementEffectMoodSound.put("tick_delay",
                            biomeRegistryEntryElementEffectMoodSound.getTickDelay());
                    nbtBiomeRegistryEntryElementEffectMoodSound.put("offset",
                            biomeRegistryEntryElementEffectMoodSound.getOffset());
                    nbtBiomeRegistryEntryElementEffectMoodSound.put("block_search_extent",
                            biomeRegistryEntryElementEffectMoodSound.getBlockSearchExtent());

                    nbtBiomeRegistryEntryElementEffect.put("mood_sound",
                            nbtBiomeRegistryEntryElementEffectMoodSound);
                }
                if (biomeRegistryEntryElementEffect.hasParticle()) {
                    BiomeRegistryEntryElementEffectParticle biomeRegistryEntryElementEffectParticle =
                            biomeRegistryEntryElementEffect.getParticle();
                    NBTCompound nbtBiomeRegistryEntryElementEffectParticle = new NBTCompound();
                    nbtBiomeRegistryEntryElementEffectParticle.put("probability",
                            biomeRegistryEntryElementEffectParticle.getProbability());
                    nbtBiomeRegistryEntryElementEffectParticle.put("options", new NBTCompound() {{
                        putAll(biomeRegistryEntryElementEffectParticle.getOptions());
                    }});

                    nbtBiomeRegistryEntryElementEffect.put("particle", nbtBiomeRegistryEntryElementEffectParticle);
                }

                nbtBiomeRegistryEntryElementEffects.add(nbtBiomeRegistryEntryElementEffect);
            }

            nbtBiomeRegistryEntryElement.put("particle", nbtBiomeRegistryEntryElementEffects);
            nbtBiomeRegistryEntry.put("element", nbtBiomeRegistryEntryElement);
            nbtBiomeRegistryEntries.add(nbtBiomeRegistryEntry);
        }
        nbtBiomeRegistry.put("value", nbtBiomeRegistryEntries);


        NBTCompound nbtChatRegistry = new NBTCompound();
        nbtChatRegistry.put("type", "minecraft:chat_type");
        NBTList nbtChatRegistryEntries = new NBTList(TagType.COMPOUND);
        for (int i=0; i<registryCodec.getChatTypes().length; i++) {
            ChatRegistryEntry chatRegistryEntry = registryCodec.getChatTypes()[i];
            NBTCompound nbtChatTypeRegistryEntry = new NBTCompound();
            nbtChatTypeRegistryEntry.put("name", chatRegistryEntry.getName());
            nbtChatTypeRegistryEntry.put("id", chatRegistryEntry.getId());

            ChatRegistryEntryElement chatRegistryEntryElement = chatRegistryEntry.getElement();
            NBTCompound nbtChatTypeRegistryEntryElement = new NBTCompound();

            if (chatRegistryEntryElement.hasChat()) {
                ChatRegistryEntryElementChat chatRegistryEntryElementChat = chatRegistryEntryElement.getChat();
                NBTCompound nbtChatTypeRegistryEntryElementChat = new NBTCompound();
                if (chatRegistryEntryElementChat.hasDecorations()) {
                    ChatRegistryEntryElementChatDecorations chatRegistryEntryElementChatDecorations =
                            chatRegistryEntryElementChat.getDecorations();
                    NBTCompound nbtChatTypeRegistryEntryElementChatDecorations = new NBTCompound();
                    nbtChatTypeRegistryEntryElementChatDecorations.put("parameters",
                            new NBTList(TagType.STRING) {{
                                addAll(Arrays.asList(chatRegistryEntryElementChatDecorations.getParameters()));
                    }});
                    nbtChatTypeRegistryEntryElementChatDecorations.put("translation_key",
                            chatRegistryEntryElementChatDecorations.getTranslationKey());

                    ChatRegistryEntryElementChatDecorationsStyle chatRegistryEntryElementChatDecorationsStyle =
                            chatRegistryEntryElementChatDecorations.getStyle();
                    NBTCompound nbtChatTypeRegistryEntryElementChatDecorationsStyle = new NBTCompound();
                    nbtChatTypeRegistryEntryElementChatDecorations.put("style",
                            nbtChatTypeRegistryEntryElementChatDecorationsStyle);

                    nbtChatTypeRegistryEntryElementChat.put("decorations", nbtChatTypeRegistryEntryElementChatDecorations);
                }
                nbtChatTypeRegistryEntryElement.put("chat", nbtChatTypeRegistryEntryElementChat);
            }
            if (chatRegistryEntryElement.hasNarration()) {
                ChatRegistryEntryElementNarration chatRegistryEntryElementNarration = chatRegistryEntryElement.getNarration();
                NBTCompound nbtChatTypeRegistryEntryElementNarration = new NBTCompound();
                if (chatRegistryEntryElementNarration.hasDecorations()) {
                    ChatRegistryEntryElementNarrationDecorations chatRegistryEntryElementNarrationDecorations =
                            chatRegistryEntryElementNarration.getDecorations();
                    NBTCompound nbtChatTypeRegistryEntryElementNarrationDecorations = new NBTCompound();
                    nbtChatTypeRegistryEntryElementNarrationDecorations.put("parameters",
                            new NBTList(TagType.STRING) {{
                                addAll(Arrays.asList(chatRegistryEntryElementNarrationDecorations.getParameters()));
                            }});
                    nbtChatTypeRegistryEntryElementNarrationDecorations.put("translation_key",
                            chatRegistryEntryElementNarrationDecorations.getTranslationKey());

                    ChatRegistryEntryElementNarrationDecorationsStyle chatRegistryEntryElementNarrationDecorationsStyle =
                            chatRegistryEntryElementNarrationDecorations.getStyle();
                    NBTCompound nbtChatTypeRegistryEntryElementNarrationDecorationsStyle = new NBTCompound();
                    nbtChatTypeRegistryEntryElementNarrationDecorations.put("style",
                            nbtChatTypeRegistryEntryElementNarrationDecorationsStyle);

                    nbtChatTypeRegistryEntryElementNarration.put("decorations", nbtChatTypeRegistryEntryElementNarrationDecorations);
                }
                nbtChatTypeRegistryEntryElement.put("narration", nbtChatTypeRegistryEntryElementNarration);
            }
            if (chatRegistryEntryElement.hasOverlay()) {
                ChatRegistryEntryElementOverlay chatRegistryEntryElementOverlay = chatRegistryEntryElement.getOverlay();
                NBTCompound nbtChatRegistryEntryElementOverlay = new NBTCompound();

                nbtChatTypeRegistryEntryElement.put("overlay", nbtChatRegistryEntryElementOverlay);
            }
            nbtChatTypeRegistryEntry.put("element", nbtChatTypeRegistryEntryElement);
            nbtChatRegistryEntries.add(nbtChatTypeRegistryEntry);
        }
        nbtChatRegistry.put("value", nbtChatRegistryEntries);


        NBTCompound nbtCompound = new NBTCompound();
        nbtCompound.put("minecraft:dimension_type", nbtDimensionTypeRegistry);
        nbtCompound.put("minecraft:worldgen/biome", nbtBiomeRegistry);
        nbtCompound.put("minecraft:chat_type", nbtChatRegistry);


        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            NBTWriter.write(nbtCompound, byteArrayOutputStream, "registryCodec", false);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e); // Should be impossible
        }
    }
}
