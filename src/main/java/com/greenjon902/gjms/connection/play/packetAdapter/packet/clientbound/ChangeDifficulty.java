package com.greenjon902.gjms.connection.play.packetAdapter.packet.clientbound;

import com.greenjon902.gjms.common.Difficulty;
import com.greenjon902.gjms.connection.ClientboundPacket;

public class ChangeDifficulty extends ClientboundPacket {
    public final Difficulty difficulty;
    public final boolean isDifficultyLocked;

    public ChangeDifficulty(Difficulty difficulty, boolean isDifficultyLocked) {
        this.difficulty = difficulty;
        this.isDifficultyLocked = isDifficultyLocked;
    }

    @Override
    public int getPacketId() {
        return 0x0B;
    }

    @Override
    public String toString() {
        return "ChangeDifficulty{" +
                "difficulty=" + difficulty +
                ", isDifficultyLocked=" + isDifficultyLocked +
                '}';
    }
}
