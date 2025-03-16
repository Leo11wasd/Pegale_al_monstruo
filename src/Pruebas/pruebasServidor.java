package Pruebas;

import jakarta.jms.Connection;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import Mensajes.HitMessage;
import Mensajes.LoginResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class pruebasServidor {
    // Datos de conexión del login y hit
    private static final String SERVER_IP = "localhost";
    private static final int LOGIN_PORT = 49152;
    private static final int HIT_PORT = 49153;
    private static final String CLIENT_ID = "TestClient1";

    // Variables para JMS (se obtienen del LoginResponse)
    private static String brokerUrl;
    private static String subjectMonstruo;
    private static String subjectGanador;

    // Contador para simular el "tiempo" secuencial en el hit
    private static int hitCounter = 1;

    // Conexión persistente para enviar hits
    private static Socket hitSocket;
    private static ObjectOutputStream hitOut;

    public static void main(String[] args) {
        // Paso 1: Hacer login vía TCP
        LoginResponse loginResponse = doLogin();
        if (loginResponse == null) {
            System.out.println("Error en el login.");
            return;
        }
        System.out.println("LoginResponse recibido: " + loginResponse);

        // Extraer datos del LoginResponse
        brokerUrl = loginResponse.getUrl();
        subjectMonstruo = loginResponse.getSubjectMosntruos();
        subjectGanador  = loginResponse.getSubjectGanador();

        // Establecer conexión persistente para enviar hits
        try {
            hitSocket = new Socket(SERVER_IP, HIT_PORT);
            hitOut = new ObjectOutputStream(hitSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Paso 2: Lanzar hilos independientes para escuchar cada tópico

        // Hilo para recibir mensajes del tópico de monstruos
        Thread monstruoListenerThread = new Thread(() -> {
            try {
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
                Connection jmsConnection = factory.createConnection();
                jmsConnection.start();
                Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination monstruoTopic = jmsSession.createTopic(subjectMonstruo);
                MessageConsumer monstruoConsumer = jmsSession.createConsumer(monstruoTopic);

                while (true) {
                    Message message = monstruoConsumer.receive();
                    if (message instanceof TextMessage) {
                        String monstruoMsg = ((TextMessage) message).getText();
                        System.out.println("Mensaje de monstruo recibido: " + monstruoMsg);
                        // Al recibir un mensaje de monstruo, se envía un hit
                        sendHit();
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        // Hilo para recibir mensajes del tópico de ganador
        Thread ganadorListenerThread = new Thread(() -> {
            try {
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
                Connection jmsConnection = factory.createConnection();
                jmsConnection.start();
                Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination ganadorTopic = jmsSession.createTopic(subjectGanador);
                MessageConsumer ganadorConsumer = jmsSession.createConsumer(ganadorTopic);

                while (true) {
                    Message message = ganadorConsumer.receive();
                    if (message instanceof TextMessage) {
                        String ganadorMsg = ((TextMessage) message).getText();
                        System.out.println("Mensaje de ganador recibido: " + ganadorMsg);
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        // Iniciar ambos hilos
        monstruoListenerThread.start();
        ganadorListenerThread.start();

        // Mantener el hilo principal activo
        System.out.println("Cliente en ejecución. Presiona ENTER para salir...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Al salir, cerrar la conexión persistente de hits
        try {
            if (hitOut != null) hitOut.close();
            if (hitSocket != null) hitSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza el proceso de login conectándose al servidor de login.
     */
    private static LoginResponse doLogin() {
        try (Socket socket = new Socket(SERVER_IP, LOGIN_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Enviar el ID del cliente (usando writeUTF, como espera el servidor)
            out.writeUTF(CLIENT_ID);
            out.flush();

            // Leer el objeto LoginResponse
            Object response = in.readObject();
            if (response instanceof LoginResponse) {
                return (LoginResponse) response;
            } else {
                System.out.println("Respuesta inesperada en el login.");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Envía un hit al servidor de golpes usando la conexión persistente.
     */
    private static void sendHit() {
        // Crea un objeto HitMessage con el ID del cliente y el contador secuencial
        HitMessage hit = new HitMessage(CLIENT_ID, hitCounter);
        hitCounter++; // Incrementa el contador para el siguiente hit

        try {
            hitOut.writeObject(hit);
            hitOut.flush();
            System.out.println("Enviado hit: " + hit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
