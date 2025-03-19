package Estress;

import Servidor.GameMaster;

public class StressLauncher {
    int jugadores;
    int golpes;
    int ronda;
    int tiempoEnvios;
    ClienteEstresador[] clientes;

    public StressLauncher(int jugadores,int golpes, int ronda, int tiempoEnvios) {
        this.jugadores = jugadores;
        this.golpes = golpes;
        this.ronda = ronda;
        this.tiempoEnvios = tiempoEnvios;
        clientes = new ClienteEstresador[jugadores];
    }

    public void start(){

        //Inicializa al servidor
        GameMasterEstresa gm = new GameMasterEstresa(golpes, tiempoEnvios);

        new Thread(() -> gm.startLoginListner()).start();
        new Thread(() -> gm.startHitListner()).start();
        gm.play();

        //lanza a los jugadores
        for (int i = 0; i < jugadores; i++) {
            ClienteEstresador clte = new ClienteEstresador("127.0.0.1", 49152, i,golpes, ronda);
            clientes[i] = clte;
            clte.start();

        }
    }
    public void estaditicas(){

    }

    public static void main(String[] args) {

    }
}
