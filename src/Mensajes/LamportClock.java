package Mensajes;

public class LamportClock {
    private int time;

    // Retorna el valor actual (para incluir en un mensaje)
    public synchronized int getTime() {
        return time;
    }

    // Se invoca cuando generas un evento local (p. ej., el jugador golpea).
    public synchronized void increment() {
        time++;
    }

    // Se invoca cuando recibes un evento remoto con timestamp remoto.
    // Ajusta el tiempo local para que sea mayor que el remoto.
    public synchronized void update(int remoteTime) {
        time = Math.max(time, remoteTime) + 1;
    }
}
