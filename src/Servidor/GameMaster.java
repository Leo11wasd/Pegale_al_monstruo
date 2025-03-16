package Servidor;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.HashMap;

import java.net.*;
import java.io.*;

public class GameMaster {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subjectMonstruo = "PegaleAlMonstruo";
    private static String subjectGanador ="Ganador";


    private int golpes; //golpes necesarios para ganar un juego
    private int tiempoEnvios;
    private HashMap<String, Integer> puntaje;

    //Objetos del JMS
    private Connection connection;
    private Session session;
    private MessageProducer producerMonstruo;
    private MessageProducer producerGanador;

    //Comunicacion TCP
    private int puertoLogin=49152;
    private int puertoHit=49153;


    public GameMaster(int golpes,int tiempoEnvios) {
        this.golpes = golpes;
        this.tiempoEnvios = tiempoEnvios;

        try {
            // 1. Crear la conexión
            ConnectionFactory factory = new ActiveMQConnectionFactory(url);
            this.connection = factory.createConnection();
            this.connection.start();

            // 2. Crear la sesión
            //    false = no transaccional
            //    Session.AUTO_ACKNOWLEDGE = modo de confirmación automático
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 3. Crear los destinos (tópicos)
            Destination monstruoDestination = session.createTopic(subjectMonstruo);
            Destination ganadorDestination  = session.createTopic(subjectGanador);

            // 4. Crear los productores para cada tópico
            this.producerMonstruo = session.createProducer(monstruoDestination);
            this.producerGanador  = session.createProducer(ganadorDestination);



        } catch (JMSException e) {
            e.printStackTrace();
        }


    }

    public void startLoginListner() {
        try {
            ServerSocket listenSocket = new ServerSocket(puertoLogin);
            while (true) {
                //System.out.println("Waiting for messages...");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                LoginConnection c = new LoginConnection(clientSocket,this);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }
    public void startHitListner() {
        try {
            ServerSocket listenSocket = new ServerSocket(puertoHit);
            while (true) {
                //System.out.println("Waiting for messages...");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                HitConnection c = new HitConnection(clientSocket,this);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }

    public void play() {


        while(true){

        }
    }

    public boolean idValido(String id) {
        return !puntaje.containsKey(id);
    }

    public static String getUrl() {
        return url;
    }

    public static String getSubjectMonstruo() {
        return subjectMonstruo;
    }

    public static String getSubjectGanador() {
        return subjectGanador;
    }


    public int getPuertoHit() {
        return puertoHit;
    }

    public void registrarJugador(String idJugador) {
        puntaje.put(idJugador,0);
    }
}
