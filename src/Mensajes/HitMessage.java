package Mensajes;

import java.io.Serializable;

public class HitMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerId;
    private int lamportTimestamp;  // Tiempo lógico

    public HitMessage(String playerId, int lamportTimestamp) {
        this.playerId = playerId;
        this.lamportTimestamp = lamportTimestamp;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getLamportTimestamp() {
        return lamportTimestamp;
    }
}
