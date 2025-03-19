package Servidor;

import Mensajes.LoginResponse;

import java.net.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginConnection extends Thread {

    private Socket clientSocket;
    private GameMaster gameMaster;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public LoginConnection(Socket clientSocket, GameMaster gameMaster) {
        this.clientSocket = clientSocket;
        this.gameMaster = gameMaster;


        try {
            in  = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.out.println(e);
        }
    }
    @Override
    public void run() {
        try {
            String id = in.readUTF();

            LoginResponse lr;
            String ip = InetAddress.getLocalHost().getHostAddress();
            if(gameMaster.idValido(id)){
                //"tcp://" + ip + ":61616"
                lr = new LoginResponse("tcp://" + ip + ":61616",GameMaster.getSubjectMonstruo(),GameMaster.getSubjectGanador(),gameMaster.getPuertoHit(),true,ip);
                gameMaster.registrarJugador(id);
            }
            else{
                //gameMaster.registrarJugador(id);
                //lr = new LoginResponse("","","",-1,false,"");
                lr = new LoginResponse("tcp://" + ip + ":61616",GameMaster.getSubjectMonstruo(),GameMaster.getSubjectGanador(),gameMaster.getPuertoHit(),true,ip);
            }

            out.writeObject(lr);

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
