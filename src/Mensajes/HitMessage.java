package Mensajes;

import java.io.Serializable;

public class HitMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerId;
    private int lamportTimestamp;
    private int ronda;

    public HitMessage(String playerId, int lamportTimestamp, int ronda) {
        this.playerId = playerId;
        this.lamportTimestamp = lamportTimestamp;
        this.ronda = ronda;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getLamportTimestamp() {
        return lamportTimestamp;
    }
    public int getRonda() {
        return ronda;
    }
}
