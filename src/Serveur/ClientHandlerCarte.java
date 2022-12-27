package Serveur;

import Classe.Carte;
import Classe.SourceTaches;
import Classe.Utilisateur;
import database.facility.BD_Bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerCarte extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    BD_Bean BC;

    public ClientHandlerCarte(SourceTaches tachesAFaire, BD_Bean BC) {
        this.tachesAExecuter = tachesAFaire;
        this.BC = BC;
    }

    @Override
    public void run() {
        String received;
        int connexion = 1;
        while (connexion == 1) {
            BC.setTable("");
            BC.setColumns("");
            BC.setValues("");
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheEnCours = tachesAExecuter.getTache();


                //creation des flux
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) ois.readObject();
                System.out.println("received = " + received);

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                    System.out.println("Connexion fermée");
                    connexion = 0;
                }

                if(received.equals("VERIFICATION")) {
                    Carte verifCarte = (Carte) ois.readObject();

                    BC.setTable("Carte");
                    BC.setCondition("nomClient = '" + verifCarte.get_nomClient() + "' and " + "motDePasse = '" + verifCarte.get_mdp() + "' and numeroCarte = '" + verifCarte.get_numeroCarte() + "'");
                    ResultSet rs = BC.Select(false);
                    int compteur = 0;
                    while (rs.next()) {
                        compteur++;
                    }

                    if (compteur == 1) {
                        oos.writeObject("OK");
                    }
                    else {
                        oos.writeObject("NOK");
                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
