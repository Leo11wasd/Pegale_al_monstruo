package Servidor;

import Mensajes.HitMessage;

import java.net.*;
import java.io.*;

public class HitConnection extends Thread {

    private Socket clientSocket;
    private GameMaster gameMaster;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public HitConnection(Socket clientSocket, GameMaster gameMaster) {
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

                gameMaster.añadePuntaje(id, tiempo);
                System.out.println(id + " " + tiempo);
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
