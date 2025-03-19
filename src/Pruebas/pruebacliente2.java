package Pruebas;
import Cliente.Jugador;
public class pruebacliente2 {
    public static void main(String[] args) {
        Thread thread2 = new Thread(() -> {
            Jugador jugador2 = new Jugador("127.0.0.1", 49152, 1);
            jugador2.hacer_login();
            jugador2.inicializa_escucha_Monstruo();
            jugador2.inicializa_escucha_Ganador();
        });
        thread2.start();

    }
}
