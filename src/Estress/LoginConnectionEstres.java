package Estress;

import Mensajes.LoginResponse;

import java.net.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginConnectionEstres extends Thread {

    private Socket clientSocket;
    private GameMasterEstresa gameMaster;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public LoginConnectionEstres(Socket clientSocket, GameMasterEstresa gameMaster) {
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
                lr = new LoginResponse("tcp://" + ip + ":61616",GameMasterEstresa.getSubjectMonstruo(),GameMasterEstresa.getSubjectGanador(),gameMaster.getPuertoHit(),true,ip);
                gameMaster.registrarJugador(id);
            }
            else{
                //gameMaster.registrarJugador(id);
                //lr = new LoginResponse("","","",-1,false,"");
                lr = new LoginResponse("tcp://" + ip + ":61616",GameMasterEstresa.getSubjectMonstruo(),GameMasterEstresa.getSubjectGanador(),gameMaster.getPuertoHit(),true,ip);
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
