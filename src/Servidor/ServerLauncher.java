package Servidor;

public class ServerLauncher {
    public static void main(String[] args) {
        int golpes = 5;
        int tiempoEnvios = 5000;



        GameMaster gm = new GameMaster(golpes, tiempoEnvios);

        new Thread(() -> gm.startLoginListner()).start();
        new Thread(() -> gm.startHitListner()).start();
        gm.play();
    }
}
