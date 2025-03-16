package Cliente;

import java.net.*;
import java.io.*;
import Mensajes.LoginResponse;
import Mensajes.HitMessage;


public class Jugador {
    Socket socket_login = null;
    LoginResponse valores_login = null;
    int id_usuario;
    String masterIp;
    int masterPort;

    public boolean hacer_login() {

        /*establece una conexion tcp con el master y le solicita información sobre su ubicación,
            validez del id y nombres de los tópicos en atcivemq para comunicar información del estado
            del juego.
        * */
        try {
            //puerto del servidor(gameMaster)
            socket_login = new Socket(this.masterIp, this.masterPort);
            //s = new Socket("127.0.0.1", serverPort);
            ObjectOutputStream out = new ObjectOutputStream(socket_login.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket_login.getInputStream());

            out.writeUTF(Integer.toString(this.id_usuario));            // UTF is a string encoding

            this.valores_login = (LoginResponse) in.readObject();
            //System.out.println("Received data: "+this.respuesta.toString());

        } catch (UnknownHostException | ClassNotFoundException e) {
            System.out.println("Sock:" + e.getMessage());

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());

        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());

        } finally {
            if (socket_login != null) try {
                socket_login.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
        return this.valores_login.isStatus();
    }


    public void notifica_hit(int tiempo_logico) {
        try {
            socket_login = new Socket(this.valores_login.getUrl(), this.valores_login.getPuertoHit());
            //s = new Socket("127.0.0.1", serverPort);
            ObjectOutputStream out = new ObjectOutputStream(socket_login.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket_login.getInputStream());

            //creo q ya no recibe nada, solamente envia
            HitMessage mensajeHit=new HitMessage(Integer.toString(this.id_usuario),tiempo_logico);
            out.writeObject(mensajeHit);
        }      // UTF is a string encoding
        catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
    }
}