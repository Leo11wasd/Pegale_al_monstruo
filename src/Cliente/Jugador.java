package Cliente;
import java.net.*;
import java.io.*;
import Mensajes.LoginResponse;

public class Jugador {
    Socket socket_login = null;
    LoginResponse valores_login=null;
    String id_usuario;
    String masterIp;
    int masterPort;
    public boolean hacer_login() {

        try {
            //puerto del servidor(gameMaster)
            //int serverPort = 49152;
            socket_login = new Socket(this.masterIp, this.masterPort);
            //s = new Socket("127.0.0.1", serverPort);
            ObjectOutputStream out = new ObjectOutputStream(socket_login.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket_login.getInputStream());

            out.writeUTF(this.id_usuario);            // UTF is a string encoding

            this.valores_login=(LoginResponse) in.readObject();
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
}