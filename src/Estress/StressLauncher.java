package Estress;

import Servidor.GameMaster;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StressLauncher {
    int jugadores;
    int golpes;
    int ronda;
    int tiempoEnvios;
    ClienteEstresador[] clientes;

    public StressLauncher(int jugadores, int golpes, int ronda, int tiempoEnvios) {
        this.jugadores = jugadores;
        this.golpes = golpes;
        this.ronda = ronda;
        this.tiempoEnvios = tiempoEnvios;
        clientes = new ClienteEstresador[jugadores];
    }

    public void start() {

        //Inicializa al servidor
        GameMasterEstresa gm = new GameMasterEstresa(golpes, tiempoEnvios);

        new Thread(() -> gm.startLoginListner()).start();
        new Thread(() -> gm.startHitListner()).start();
        new Thread(() -> gm.play()).start();

        //System.out.println("llegue");
        //lanza a los jugadores
        for (int i = 0; i < jugadores; i++) {
            ClienteEstresador clte = new ClienteEstresador("127.0.0.1", 49152, i, golpes, ronda);
            clientes[i] = clte;
            clte.start();

        }

        //System.out.println("Estado: "+clientes[0].running);
        while (clientes[0].running) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Aquí ya se han finalizado todos los hilos de clientes, se pueden calcular las estadísticas.

        estadisticas();
    }

    public void estadisticas() {

        double tiempoJugador;
        double golpesTotal = 0;
        double tiempoTotal = 0;
        double[] promedioJ = new double[jugadores];
        double promedioLogin = 0;

        // Para la desviación estándar individual de cada jugador
        double[] desvStandJ = new double[jugadores];

        // Variables para el cálculo global de los eventos

        double sumatoriaEventosCuadrado = 0.0;
        double cuadradosLogin = 0;
        String fileName = "salida.csv";
        System.out.println("inicio proceso estadisticas");
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {  // El 'true' permite agregar al final
            writer.println("Promedios");
            for (int i = 0; i < jugadores; i++) {
                tiempoJugador = 0;
                for (double t : clientes[i].tiempos) {
                    tiempoJugador += t;
                }
                promedioJ[i] = tiempoJugador / clientes[i].contador;

                writer.println
                        (i + "," + promedioJ[i]);
                tiempoTotal += tiempoJugador;
                golpesTotal += clientes[i].contador;
                promedioLogin += clientes[i].tiempoLogin;
            }

            double promedioTotal = tiempoTotal / golpesTotal;
            writer.println
                    ("Promedio total:" + tiempoTotal / golpesTotal);
            promedioLogin = promedioLogin / jugadores;
            writer.println
                    ("Promedio Login: " + promedioLogin);

            writer.println
                    ("Desviacion Estandar");
            for (int i = 0; i < jugadores; i++) {
                double sumaCuadrados = 0.0;
                for (double t : clientes[i].tiempos) {
                    sumaCuadrados += Math.pow(t - promedioJ[i], 2);
                    sumatoriaEventosCuadrado += (t - promedioTotal) * (t - promedioTotal);
                }

                desvStandJ[i] = Math.sqrt(sumaCuadrados / clientes[i].contador);
                writer.println
                        (i + "," + desvStandJ[i]);
                cuadradosLogin += (clientes[i].tiempoLogin - promedioLogin) * (clientes[i].tiempoLogin - promedioLogin);
            }
            writer.println
                    ("Desviacion Estandar total:" + Math.sqrt(sumatoriaEventosCuadrado / golpesTotal));
            writer.println
                    ("Desviacion Estandar login:" + Math.sqrt(cuadradosLogin / jugadores));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finaliza proceso estadisticas");
    }

    public static void main(String[] args) {
        StressLauncher st = new StressLauncher(50, 5, 5, 10);
        st.start();
    }
}
