package Cliente;

import java.net.*;
import java.io.*;

import Mensajes.LoginResponse;
import Mensajes.HitMessage;


public class Jugador {
    Socket socket_login = null;
    Socket hit_socket = null;
    LoginResponse valores_login = null;
    int id_usuario;
    String master_login_Ip;
    int master_login_Port;
    int hitCounter;

    public Jugador(String master_login_Ip, int master_login_Port, int id_usuario) {
        this.master_login_Ip = master_login_Ip;
        this.master_login_Port = master_login_Port;
        this.id_usuario = id_usuario;
        this.hitCounter = 0;
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
        return this.valores_login.isStatus();
    }

    public void inicializa_escucha_Monstruo(){

    }
    public void inicializa_escucha_Ganador(){

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
}