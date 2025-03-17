package Servidor;

public class ServerLauncher {
    public static void main(String[] args) {
        int golpes = 3;
        int tiempoEnvios = 3000;



        GameMaster gm = new GameMaster(golpes, tiempoEnvios);

        new Thread(() -> gm.startLoginListner()).start();
        new Thread(() -> gm.startHitListner()).start();
        gm.play();
    }
}
