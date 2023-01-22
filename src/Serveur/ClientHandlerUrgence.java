package Serveur;

import Classe.SourceTaches;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandlerUrgence extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheUrgente;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String message;

    private int urgence;
    private int connecte;

    public ClientHandlerUrgence(SourceTaches tachesAFaire) {
        tachesAExecuter = tachesAFaire;
    }

    public int getConnecte() {
        return this.connecte;
    }

    public void sendMessage(String message){
        this.message = message;
        urgence=1;
    }

    public void Stop() throws IOException {
        if(tacheUrgente.isConnected()) {
            System.out.println(tacheUrgente.isConnected());
            System.out.println(tacheUrgente.isBound());
            System.out.println(tacheUrgente.isClosed());
            System.out.println(tacheUrgente.isInputShutdown());
            System.out.println(tacheUrgente.isOutputShutdown());
            oos.writeObject(message);
            oos.close();
            this.tacheUrgente.close();
        }
    }

    @Override
    public void run() {
        int connexion = 1;
        while (true) {
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheUrgente = tachesAExecuter.getTache();
                connecte=1;

                //ois = new ObjectInputStream(tacheUrgente.getInputStream());
                oos = new ObjectOutputStream(tacheUrgente.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                while(tacheUrgente.isConnected()) {

                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            connecte=0;
        }
    }
}
