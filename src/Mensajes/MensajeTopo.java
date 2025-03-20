package Mensajes;

import java.io.Serializable;

public class MensajeTopo implements Serializable {
    public int casilla;
    public int numTopo;
    public int Ronda;

    public MensajeTopo(int casilla, int numTopo, int Ronda) {
        this.casilla = casilla;
        this.numTopo = numTopo;
        this.Ronda = Ronda;

    }
}
