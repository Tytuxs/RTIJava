package Serveur;

import Classe.SourceTaches;
import Classe.Utilisateur;
import database.facility.BD_Bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerPaiement extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    BD_Bean BP;

    public ClientHandlerPaiement(SourceTaches tachesAFaire, BD_Bean BP) {
        this.tachesAExecuter = tachesAFaire;
        this.BP = BP;
    }

    @Override
    public void run() {
        String received;
        int connexion = 1;
        while (connexion == 1) {
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheEnCours = tachesAExecuter.getTache();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ServeurPaiement : Boucle connexion");
            try {
                //creation des flux
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) ois.readObject();
                System.out.println("received = " + received);

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                    System.out.println("Connexion fermée");
                    connexion = 0;
                }

                int ok = 0;
                if(received.equals("LOGIN")) {
                    Utilisateur user = (Utilisateur) ois.readObject();

                    ResultSet rs = this.BP.Login();
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if (user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                            System.out.println("Client trouve");
                            ok = 1;
                            break;
                        }
                    }
                }

                if(received.equals("SERVEURRESA")) {
                    System.out.println("Connexion avec ServeurReservation");
                    ok=1;
                }

                if(ok == 1) {
                    oos.writeObject("OK");

                    int continuer = 1;
                    while (continuer == 1) {
                        //reset a chaque requete pour eviter les erreurs entre les différentes requete d'un meme lance l'une à la suite de l'autre s
                        BP.setTable("");
                        BP.setColumns("");
                        BP.setValues("");

                        System.out.println("Boucle continuer");
                        //ATTENTE DE LA REQUÊTE
                        String requete = (String) ois.readObject();
                        System.out.println("Requete recue : " + requete);
                        switch (requete) {
                            case "PROOMPAY" :
                                String id = (String) ois.readObject();
                                BP.setTable("ReserActCha");
                                BP.setCondition("id = " + id);
                                BP.setValues("paye = true");
                                System.out.println("avant Update");
                                int confirmation = BP.Update();
                                System.out.println("apres Update");
                                if(confirmation==1) {
                                    oos.writeObject("OK");
                                }
                                else {
                                    oos.writeObject("NOK");
                                }
                                break;
                            case "LOGOUT" :
                                oos.writeObject("Au revoir");
                                continuer = 0;
                                break;

                            case "Exit" :
                                oos.writeObject("Au revoir");
                                continuer = 0;
                                connexion = 0;
                                break;

                            default:
                                oos.writeObject("ERROR : Invalid input");
                                break;
                        }
                    }
                }
                else {
                    System.out.println("NOK");
                    oos.writeObject("NOK");
                }
            }
            catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try
        {
            System.out.println("Fermeture des ressources");
            this.tacheEnCours.close();
            this.ois.close();
            this.oos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
