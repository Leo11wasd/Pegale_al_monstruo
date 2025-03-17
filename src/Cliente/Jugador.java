package Cliente;

import java.net.*;
import java.io.*;

import Mensajes.LoginResponse;
import Mensajes.HitMessage;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.swing.*;


public class Jugador {
    Socket socket_login = null;
    Socket hit_socket = null;
    LoginResponse valores_login = null;
    int id_usuario = 0;
    String master_login_Ip;
    int master_login_Port;
    int hitCounter;
    GridInterface tablero;


    public Jugador(String master_login_Ip, int master_login_Port, int id_usuario) {
        this.master_login_Ip = master_login_Ip;
        this.master_login_Port = master_login_Port;
        this.id_usuario = id_usuario;
        this.hitCounter = 0;
        tablero = new GridInterface();

    }

    public boolean hacer_login() {

        /*establece una conexion tcp con el master y le solicita información sobre su ubicación,
            validez del id y nombres de los tópicos en atcivemq para comunicar información del estado
            del juego.
        * */
        try {
            //puerto del servidor(gameMaster)
            this.socket_login = new Socket(this.master_login_Ip, this.master_login_Port);
            //s = new Socket("127.0.0.1", serverPort);
            ObjectOutputStream out = new ObjectOutputStream(this.socket_login.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(this.socket_login.getInputStream());

            out.writeUTF(Integer.toString(this.id_usuario));
            out.flush();


            this.valores_login = (LoginResponse) in.readObject();
            //System.out.println("Received data: "+this.respuesta.toString());

        } catch (UnknownHostException | ClassNotFoundException e) {
            System.out.println("Sock:" + e.getMessage());

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());

        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());

        } finally {
            if (this.socket_login != null) try {
                this.socket_login.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
        if(this.valores_login==null){
            System.out.println("Valores:null");
            return false;
        }
        else{
            System.out.println("se realizo la conexión con "+this.master_login_Ip+":"+this.master_login_Port);
            return this.valores_login.isStatus();
        }
    }

    public void inicializa_escucha_Monstruo() {
        Thread monstruoListenerThread = new Thread(() -> {
            try {
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.valores_login.getUrl());
                Connection jmsConnection = factory.createConnection();
                jmsConnection.start();
                Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination monstruoTopic = jmsSession.createTopic(this.valores_login.getSubjectMosntruos());
                MessageConsumer monstruoConsumer = jmsSession.createConsumer(monstruoTopic);

                while (true) {
                    Message message = monstruoConsumer.receive();
                    System.out.println("escuchando");
                    if (message instanceof TextMessage) {
                        System.out.println("Mensaje recibido: " + message.toString());
                        String monstruoMsg = ((TextMessage) message).getText();
                        System.out.println("Mensaje de monstruo recibido: " + monstruoMsg);
                        // Al recibir un mensaje de monstruo, se actualiza el grid
                        int coordenadas = Integer.parseInt(monstruoMsg);
                        //0 indexamos
                        coordenadas--;

                        //limpiamos el tablero
                        this.tablero.limpiar();
                        //ponemos el nuevo topo
                        //int i = coordenadas / 3;
                        //int j = coordenadas % 3;
                        this.tablero.muestra_topo(coordenadas);
                    }


                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        monstruoListenerThread.start();
    }

    public void inicializa_escucha_Ganador() {
        Thread ganadorListenerThread = new Thread(() -> {
            try {
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.valores_login.getUrl());
                Connection jmsConnection = factory.createConnection();
                jmsConnection.start();
                Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination ganadorTopic = jmsSession.createTopic(this.valores_login.getSubjectGanador());
                MessageConsumer ganadorConsumer = jmsSession.createConsumer(ganadorTopic);

                while (true) {
                    Message message = ganadorConsumer.receive();
                    if (message instanceof TextMessage) {
                        String ganadorMsg = ((TextMessage) message).getText();
                        //el mensaje que llega es "El ganador de este juego es : " + ganadorActual+". El siguiente juego comenzara pronto."
                        //System.out.println("Mensaje de ganador recibido: " + ganadorMsg);
                        int id_ganador = Integer.parseInt(ganadorMsg);
                        System.out.println("idganador "+id_ganador);
                        if(id_ganador!=-1){
                            if (id_ganador == this.id_usuario) {
                                JOptionPane.showMessageDialog(null, "Dale campeon, dale campeon, dale campeon, dale campeon", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Perdiste :c", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        ganadorListenerThread.start();
    }


    public void notifica_hit() {
        try {
            if (this.hit_socket == null) {
                this.hit_socket = new Socket(this.valores_login.getUrl(), this.valores_login.getPuertoHit());
            }
            //s = new Socket("127.0.0.1", serverPort);
            ObjectOutputStream out = new ObjectOutputStream(this.socket_login.getOutputStream());
            //creo q ya no recibe nada, solamente envia.
            HitMessage mensajeHit = new HitMessage(Integer.toString(this.id_usuario), this.hitCounter);
            this.hitCounter++;
            out.writeObject(mensajeHit);
            out.flush();
            System.out.println("Se envió el hit");
        }      // UTF is a string encoding
        catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Jugador jugador = new Jugador("127.0.0.1", 49152, 0);
        jugador.hacer_login();
        jugador.inicializa_escucha_Monstruo();
        jugador.inicializa_escucha_Ganador();
        System.out.println("Puerto hit "+jugador.valores_login.getPuertoHit());
        System.out.println("subject ganador "+jugador.valores_login.getSubjectGanador());
        System.out.println("subject monstros "+jugador.valores_login.getSubjectMosntruos());
        System.out.println("url "+jugador.valores_login.getUrl());
    }
}