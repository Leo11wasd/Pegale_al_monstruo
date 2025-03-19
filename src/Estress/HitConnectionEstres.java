package Estress;

import Mensajes.HitMessage;
import Servidor.GameMaster;

import java.net.*;
import java.io.*;

public class HitConnectionEstres extends Thread {

    private Socket clientSocket;
    private GameMasterEstresa gameMaster;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public HitConnectionEstres(Socket clientSocket, GameMasterEstresa gameMaster) {
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
            while (true) {
                HitMessage msg = (HitMessage) in.readObject();

                String id = msg.getPlayerId();
                int tiempo = msg.getLamportTimestamp();
                int ronda = msg.getRonda();
                gameMaster.añadePuntaje(id, tiempo,ronda);

                out.writeBoolean(true);
                out.flush();
                //System.out.println(id + " " + tiempo+" "+ronda);
            }


        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
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
