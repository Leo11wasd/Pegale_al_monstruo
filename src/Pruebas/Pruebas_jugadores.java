package Pruebas;

import Cliente.Jugador;

public class Pruebas_jugadores extends Thread {
    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            Jugador jugador1 = new Jugador("127.0.0.1", 49152, 0);
            jugador1.hacer_login();
            jugador1.inicializa_escucha_Monstruo();
            jugador1.inicializa_escucha_Ganador();
            // Aquí puedes usar jugador1 dentro del hilo
        });
        thread.start();


        Thread thread2 = new Thread(() -> {
            Jugador jugador2 = new Jugador("127.0.0.1", 49152, 1);
            jugador2.hacer_login();
            jugador2.inicializa_escucha_Monstruo();
            jugador2.inicializa_escucha_Ganador();
        });
        thread2.start();


        Thread thread3 = new Thread(() -> {
            Jugador jugador3 = new Jugador("127.0.0.1", 49152, 2);
            jugador3.hacer_login();
            jugador3.inicializa_escucha_Monstruo();
            jugador3.inicializa_escucha_Ganador();

        });
        thread3.start();


        Thread thread4 = new Thread(() -> {
            Jugador jugador4 = new Jugador("127.0.0.1", 49152, 3);
            jugador4.hacer_login();
            jugador4.inicializa_escucha_Monstruo();
            jugador4.inicializa_escucha_Ganador();
        });
        thread4.start();


        Thread thread5 = new Thread(() -> {
            Jugador jugador5 = new Jugador("127.0.0.1", 49152, 4);
            jugador5.hacer_login();
            jugador5.inicializa_escucha_Monstruo();
            jugador5.inicializa_escucha_Ganador();
        });
        thread5.start();

    }
}
