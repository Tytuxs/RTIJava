package Serveur;

import Classe.Carte;
import Classe.SourceTaches;
import database.facility.BD_Bean;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerBanque extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    BD_Bean BB;

    public ClientHandlerBanque(SourceTaches tachesAFaire, BD_Bean BB) {
        this.tachesAExecuter = tachesAFaire;
        this.BB = BB;
    }

    @Override
    public void run() {
        String received;
        while(true) {
            try {
                tacheEnCours = tachesAExecuter.getTache();
                // DataInputStream ois = new DataInputStream(new BufferedInputStream
                // (CSocket.getInputStream()));
                // Deprecated !
                ObjectOutputStream oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(tacheEnCours.getInputStream());
                // DataOutputStream oos = new DataOutputStream
                // (new BufferedOutputStream (CSocket.getOutputStream()));
                int connexion = 1;
                while(connexion == 1) {
                    received = (String) ois.readObject();
                    System.out.println("received = " + received);

                    if (received.equals("Exit")) {
                        System.out.println("Client " + this.tacheEnCours + " quitte...");
                        System.out.println("Fermeture connexion.");
                        System.out.println("Connexion fermée");
                    }

                    if (received.equals("VERIFICATION MDP")) {
                        Carte carte = (Carte) ois.readObject();
                        BB.setTable("bank");
                        BB.setCondition("numeroCarte = " + "'" + carte.get_numeroCarte() + "' and mdp = '" + carte.get_mdp() + "'");

                        ResultSet rs = BB.Select(false);
                        int compteur = 0;
                        while (rs.next()) {
                            compteur++;
                        }
                        if (compteur > 0) {
                            oos.writeObject("OK");
                        }
                        else {
                            oos.writeObject("NOK");
                        }
                    }
                    connexion = 0;
                }
                ois.close();
                oos.close();
                tacheEnCours.close();
            } catch (IOException e) {
                System.err.println("Erreur ! ? [" + e + "]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




        /*String received;
        while(true) {
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheEnCours = tachesAExecuter.getTache();

                //creation des flux
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                BB.setTable("");
                BB.setColumns("");
                BB.setValues("");
                received = (String) ois.readObject();
                System.out.println("received = " + received);

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                }

                if (received.equals("VERIFICATION")) {
                    oos.writeObject("OK");
                    Carte verifCarte = (Carte) ois.readObject();
                    System.out.println("nomClient = " + verifCarte.get_nomClient());
                    System.out.println("numeroCarte = " + verifCarte.get_numeroCarte());
                    System.out.println("mdp = " + verifCarte.get_mdp());

                    BB.setTable("bank");
                    BB.setCondition("mdp = '" + verifCarte.get_mdp() + "' and numeroCarte = '" + verifCarte.get_numeroCarte() + "'");
                    ResultSet rs = BB.Select(false);
                    int compteur = 0;
                    while (rs.next()) {
                        compteur++;
                    }

                    if (compteur == 1) {
                        oos.writeObject("OK");
                    } else {
                        oos.writeObject("NOK");
                    }
                } else {
                    oos.writeObject("NOK");
                }
                oos.close();
                ois.close();
                tacheEnCours.close();
            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
}

