package Serveur;

import Classe.Carte;
import Classe.ReserActCha;
import Classe.SourceTaches;
import ClassesCrypto.RequeteDigest;
import database.facility.BD_Bean;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ClientHandlerPaiement extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream oisReservation;
    private ObjectOutputStream oosReservation;
    private Socket sCarte;
    private ObjectInputStream oisCarte;
    private ObjectOutputStream oosCarte;
    BD_Bean BP;
    private static String codeProvider = "BC";

    public ClientHandlerPaiement(SourceTaches tachesAFaire, BD_Bean BP) {
        this.tachesAExecuter = tachesAFaire;
        this.BP = BP;
    }

    @Override
    public void run() {
        Security.addProvider(new BouncyCastleProvider());
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
                oisReservation = new ObjectInputStream(tacheEnCours.getInputStream());
                oosReservation = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) oisReservation.readObject();
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
                    /*Utilisateur user = (Utilisateur) oisReservation.readObject();

                    ResultSet rs = this.BP.Login();
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if (user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                            System.out.println("Client trouve");
                            ok = 1;
                            break;
                        }
                    }*/
                    RequeteDigest user = (RequeteDigest) oisReservation.readObject();
                    ResultSet rs = this.BP.Login();
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if (user.getUtilisateur().equals(userbd)) {
                            //verif de pwdbd en creant un digest
                            MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider);
                            md.update(user.getUtilisateur().getBytes());
                            md.update(pwdbd.getBytes());
                            byte[] mdLocal = md.digest();
                            if(MessageDigest.isEqual(user.getMdp(),mdLocal)) {
                                ok = 1;
                            }
                            System.out.println("message recu = " + Arrays.toString(user.getMdp()));
                            System.out.println("message local = " + Arrays.toString(mdLocal));
                            break;
                        }
                    }
                }

                if(received.equals("SERVEURRESA")) {
                    System.out.println("Connexion avec ServeurReservation");
                    ok=1;
                }

                if(ok == 1) {
                    oosReservation.writeObject("OK");

                    int continuer = 1;
                    while (continuer == 1) {
                        //reset a chaque requete pour eviter les erreurs entre les différentes requete d'un meme lance l'une à la suite de l'autre
                        BP.setTable("");
                        BP.setColumns("");
                        BP.setValues("");

                        System.out.println("Boucle continuer");
                        //ATTENTE DE LA REQUÊTE du SERVEURRESERVATION ou de APPLIPaiement
                        String requete = (String) oisReservation.readObject();
                        System.out.println("Requete recue : " + requete);
                        switch (requete) {
                            case "PROOMPAY" :
                                String nomClient = (String) oisReservation.readObject();
                                String id = (String) oisReservation.readObject();
                                float paiement = (Float) oisReservation.readObject();
                                String CB = (String) oisReservation.readObject();
                                String CBMDP = (String) oisReservation.readObject();

                                //verification de la carteBancaire
                                InetAddress ip = InetAddress.getByName("localhost");
                                sCarte = new Socket(ip, 8000);
                                System.out.println("sPaiement = " + sCarte);
                                System.out.println("Connexion au ServeurCarte avec le ServeurPaiement");

                                //penser à inverser les flux si erreur de création
                                this.oosCarte = new ObjectOutputStream(this.sCarte.getOutputStream());
                                this.oisCarte = new ObjectInputStream(this.sCarte.getInputStream());

                                Carte carte = new Carte();
                                carte.set_nomClient(nomClient);
                                carte.set_numeroCarte(CB);
                                carte.set_mdp(CBMDP);

                                oosCarte.writeObject("VERIFICATION");
                                if(oisCarte.readObject().equals("OK")) {
                                    oosCarte.writeObject(carte);
                                    String confirmation = (String) oisCarte.readObject();
                                    if (confirmation.equals("OK")) {
                                        BP.setTable("ReserActCha");
                                        BP.setCondition("id = " + id);

                                        ResultSet rs = BP.Select(false);
                                        float dejaPaye = 0;

                                        while (rs.next()) {
                                            dejaPaye = rs.getFloat("dejaPaye");
                                        }

                                        paiement += dejaPaye;

                                        BP.setValues("dejaPaye = " + paiement);
                                        System.out.println("avant Update");
                                        int confir = BP.Update();
                                        System.out.println("apres Update");
                                        if (confir == 1) {
                                            oosReservation.writeObject("OK");
                                        } else {
                                            oosReservation.writeObject("NOK");
                                        }
                                    } else {
                                        oosReservation.writeObject("Erreur carte bancaire");
                                    }
                                }
                                oisCarte.close();
                                oosCarte.close();
                                sCarte.close();
                                break;

                            case "LISTPAY" :
                                BP.setTable("Reseractcha");
                                BP.setCondition("type = 'Chambre' and dejapaye < prixCha");
                                ResultSet resultatLISTPAY = BP.Select(false);
                                while (resultatLISTPAY.next()) {
                                    ReserActCha reservation = new ReserActCha();
                                    reservation.set_id(resultatLISTPAY.getInt("id"));
                                    reservation.set_numChambre(resultatLISTPAY.getInt("numChambre"));
                                    reservation.set_prixCha(resultatLISTPAY.getFloat("prixCha"));
                                    reservation.set_persRef(resultatLISTPAY.getString("PersRef"));
                                    reservation.set_dejaPaye(resultatLISTPAY.getFloat("dejaPaye"));

                                    oosReservation.writeObject(reservation);
                                }
                                oosReservation.writeObject(null);

                                break;

                            case "LOGOUT" :
                                oosReservation.writeObject("Au revoir");
                                continuer = 0;
                                break;

                            case "Exit" :
                                oosReservation.writeObject("Au revoir");
                                continuer = 0;
                                connexion = 0;
                                break;

                            default:
                                oosReservation.writeObject("ERROR : Invalid input");
                                break;
                        }
                    }
                }
                else {
                    System.out.println("NOK");
                    oosReservation.writeObject("NOK");
                }
            }
            catch (IOException | SQLException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        }

        try
        {
            System.out.println("Fermeture des ressources");
            this.tacheEnCours.close();
            this.oisReservation.close();
            this.oosReservation.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
