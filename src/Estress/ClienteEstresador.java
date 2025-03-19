package Estress;

import Cliente.GridInterface;
import Mensajes.HitMessage;
import Mensajes.LoginResponse;
import Mensajes.MensajeTopo;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class ClienteEstresador extends Thread{

    Socket socket_login = null;
    Socket hit_socket = null;
    LoginResponse valores_login = null;
    int id_usuario = 0;
    String master_login_Ip;
    int master_login_Port;


    int topoActual;
    int rondaActual;
    ObjectOutputStream outHit;
    ObjectInputStream inHit;

    //Para estadisticas
    LinkedList<Double> tiempos;
    int contador=0;
    double t2,t1;
    int rondaCorte;
    boolean running;
    double tiempoLogin;

    public ClienteEstresador(String master_login_Ip, int master_login_Port, int id_usuario,int golpes,int rondas) {
        this.master_login_Ip = master_login_Ip;
        this.master_login_Port = master_login_Port;
        this.id_usuario = id_usuario;
        this.rondaCorte=rondas+1;

        tiempos=new LinkedList<>();

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
            t1 = System.currentTimeMillis();
            out.writeUTF(Integer.toString(this.id_usuario));
            out.flush();


            this.valores_login = (LoginResponse) in.readObject();
            t2 = System.currentTimeMillis();

            tiempoLogin = t2-t1;

            this.hit_socket = new Socket(this.valores_login.getIp(), this.valores_login.getPuertoHit());

            //System.out.println("Received data: "+this.respuesta.toString());

            outHit = new ObjectOutputStream(this.hit_socket.getOutputStream());
            inHit = new ObjectInputStream(this.hit_socket.getInputStream());
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
                factory.setTrustAllPackages(true);
                Connection jmsConnection = factory.createConnection();
                jmsConnection.start();
                Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination monstruoTopic = jmsSession.createTopic(this.valores_login.getSubjectMosntruos());
                MessageConsumer monstruoConsumer = jmsSession.createConsumer(monstruoTopic);

                running = true;
                while (running) {
                    Message message = monstruoConsumer.receive();
                    // Ahora comprobamos si es un ObjectMessage
                    if (message instanceof ObjectMessage) {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        MensajeTopo mensajeTopo = (MensajeTopo) objectMessage.getObject(); // Cast directo

                        topoActual = mensajeTopo.numTopo;
                        rondaActual = mensajeTopo.Ronda;

                        if(rondaActual==rondaCorte){
                            running = false;
                            break;
                        }
                        //En cuanto recibe el topo notifica
                        notifica_hit();

                    }
                }
                monstruoConsumer.close();
                jmsSession.close();
                jmsConnection.close();
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

                running = true;
                while (running) {
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
                ganadorConsumer.close();
                jmsSession.close();
                jmsConnection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        ganadorListenerThread.start();
    }


    public void notifica_hit() {
        //System.out.println(this.valores_login.getIp());
        try {

            HitMessage mensajeHit = new HitMessage(Integer.toString(this.id_usuario),topoActual,rondaActual );

            t1 = System.currentTimeMillis();
            outHit.writeObject(mensajeHit);
            outHit.flush();

            boolean resp = inHit.readBoolean();
            t2 = System.currentTimeMillis();
            tiempos.add(t2-t1);
            contador++;

            //System.out.println("Se envió el hit");
        }      // UTF is a string encoding
        catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        if (!hacer_login()) {
            System.out.println("Fallo en el login para el cliente " + id_usuario);
            return;
        }
        // Inicia los listeners para monstruos y ganador
        inicializa_escucha_Monstruo();
        inicializa_escucha_Ganador();


    }
}
