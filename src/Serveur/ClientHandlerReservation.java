package Serveur;

import Classe.Chambre;
import Classe.ReserActCha;
import Classe.SourceTaches;
import Classe.Utilisateur;
import database.facility.BD_Bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClientHandlerReservation extends Thread {

    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ObjectOutputStream oosPaiement;
    private ObjectInputStream oisPaiement;
    Socket sPaiement;
    BD_Bean BR;

    // Constructor
    public ClientHandlerReservation(SourceTaches tachesAFaire, BD_Bean BR) {
        this.tachesAExecuter = tachesAFaire;
        this.BR = BR;
    }

    @Override
    public void run()
    {
        String received;
        int connexion = 1;
        while (connexion == 1)
        {
            try {
                //attends de recevoir un client
                tacheEnCours = tachesAExecuter.getTache();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Boucle connexion");
            try {
                //creation des fluxs
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) ois.readObject();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                    System.out.println("Connexion fermée");
                    connexion = 0;
                }

                if(received.equals("LOGIN")) {
                    Utilisateur user = (Utilisateur) ois.readObject();

                    ResultSet rs = this.BR.Login();
                    int ok = 0;
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if(user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                            System.out.println("Client trouve");
                            ok = 1;
                            break;
                        }
                    }

                    if(ok == 1) {
                        oos.writeObject("OK");

                        int continuer = 1;
                        while (continuer == 1) {
                            //reset a chaque requete pour eviter les erreurs entre les différentes requete d'un meme lance l'une à la suite de l'autre s
                            BR.setTable("");
                            BR.setColumns("");
                            BR.setValues("");

                            System.out.println("Boucle continuer");
                            //ATTENTE DE LA REQUÊTE
                            String requete = (String) ois.readObject();
                            System.out.println("Requete recue : " + requete);
                            switch (requete) {

                                case "BROOM" :
                                    System.out.println("BROOM");
                                    //recuperation des differents champs demandes
                                    ReserActCha reservationChambre = (ReserActCha) ois.readObject();

                                    //Creation de la date de fin grace a date et nbNuits
                                    SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(temp.parse(reservationChambre.get_date()));
                                    c.add(Calendar.DATE,reservationChambre.get_nbNuits() - 1);
                                    String dateFin=temp.format(c.getTime());

                                    //REQUETE A LA BD
                                    ResultSet resultatBROOM = BR.RequestBROOM(reservationChambre.get_categorie(),reservationChambre.get_typeCha(), reservationChambre.get_date(), dateFin);
                                    //CREATION DU MESSAGE SOUS FORME DE STRING A ENVOYER AU CLIENT
                                    while (resultatBROOM.next()) {
                                        Chambre chambre = new Chambre();
                                        chambre.set_numeroChambre(Integer.parseInt(resultatBROOM.getString("numeroChambre")));
                                        chambre.set_categorie(resultatBROOM.getString("categorie"));
                                        chambre.set_typeChambre(resultatBROOM.getString("typeChambre"));
                                        chambre.set_prixHTVA(Float.parseFloat(resultatBROOM.getString("PrixHTVA")));
                                        System.out.println("chambre envoye = " + chambre);
                                        oos.writeObject(chambre);
                                    }
                                    oos.writeObject(null);

                                    //ATTENTE DU CHOIX DU CLIENT POUR LA CHAMBRE
                                    String retour = (String) ois.readObject();

                                    if(retour.equals("OK")) {
                                        Chambre chambreAResa = (Chambre) ois.readObject();
                                        if(reservationChambre.get_typeCha().equals("Simple")) {
                                            reservationChambre.set_nbMaxCha(1);
                                        }
                                        if(reservationChambre.get_typeCha().equals("Double")) {
                                            reservationChambre.set_nbMaxCha(2);
                                        }
                                        if(reservationChambre.get_typeCha().equals("Familiale")) {
                                            reservationChambre.set_nbMaxCha(4);
                                        }
                                        //INITIALISATION DES VALEURS A METTRE DANS LA BD
                                        BR.setTable("reseractcha");
                                        BR.setColumns("`PersRef`,`type`,`numChambre`,`typeCha`,`nbMaxCha`,`nbNuit`,`dateDeb`,`prixCha`,`paye`");
                                        BR.setValues("'"+reservationChambre.get_persRef()+"'"+","
                                                +"'Chambre'"+","
                                                +chambreAResa.get_numeroChambre()+","+"'"
                                                +reservationChambre.get_typeCha()+"'"+","
                                                +reservationChambre.get_nbMaxCha()+","
                                                +reservationChambre.get_nbNuits()+","+"'"
                                                +reservationChambre.get_date()+"'"+","
                                                +chambreAResa.get_prixHTVA()+","
                                                +false);
                                        //AJOUT A LA BD
                                        System.out.println(reservationChambre.get_id());
                                        System.out.println(reservationChambre.get_numChambre());
                                        System.out.println(reservationChambre.get_prixCha());
                                        int confirmation = BR.Insert();

                                        BR.setValues("");
                                        BR.setColumns("");
                                        BR.setCondition("numChambre = " + chambreAResa.get_numeroChambre() + " AND datedeb = '" + reservationChambre.get_date() + "' AND PersRef = '" + reservationChambre.get_persRef() + "'");
                                        ReserActCha resaAEnvoye = new ReserActCha();

                                        ResultSet tmp = BR.Select(false);
                                        tmp.next();
                                        resaAEnvoye.set_persRef(tmp.getString("persref"));
                                        resaAEnvoye.set_id(Integer.parseInt(tmp.getString("id")));
                                        resaAEnvoye.set_prixCha(Float.parseFloat(tmp.getString("prixCha")));
                                        resaAEnvoye.set_numChambre(Integer.parseInt(tmp.getString("numChambre")));

                                        if(confirmation == 1) {
                                            oos.writeObject("OK");
                                            oos.writeObject(resaAEnvoye);
                                        }
                                        else {
                                            oos.writeObject("NOK");
                                        }
                                    }

                                    break;

                                case "PROOM" :
                                    //creation connexion au serveur paiement
                                    InetAddress ip = InetAddress.getByName("localhost");
                                    sPaiement = new Socket(ip, 7000);
                                    System.out.println("sPaiement = " + sPaiement);
                                    System.out.println("Connexion au ServeurPaiement avec le ServeurReservation");

                                    //RECUPERATION DU NOM DU CLIENT
                                    String nomClient = (String) ois.readObject();
                                    //INITIALISATION DES VALEURS POUR RECHERCHER DANS LA BD LES RESERVATIONS DU CLIENTS
                                    BR.setTable("ReserActCha");
                                    BR.setCondition("PersRef = '" + nomClient + "' and " + "type = 'Chambre'");
                                    //RECHERCHE DANS LA BD
                                    ResultSet resultatPROOM = BR.Select(false);
                                    //CREATION DU MESSAGE SOUS FORME DE STRING A ENVOYER AU CLIENT
                                    while (resultatPROOM.next()) {
                                        ReserActCha reservation = new ReserActCha();
                                        reservation.set_id(resultatPROOM.getInt("id"));
                                        reservation.set_numChambre(resultatPROOM.getInt("numChambre"));
                                        reservation.set_prixCha(resultatPROOM.getFloat("prixCha"));
                                        reservation.set_persRef(resultatPROOM.getString("PersRef"));
                                        reservation.set_paye(resultatPROOM.getBoolean("paye"));

                                        oos.writeObject(reservation);
                                    }
                                    oos.writeObject(null);
                                    //ATTENTE DU CLIENT POUR PAYER
                                    String paye = (String) ois.readObject();
                                    System.out.println("paye = " + paye);
                                    if(paye.equals("OK")) {
                                        String id = (String) ois.readObject();
                                        System.out.println("id = " + id);
                                        System.out.println("sPaiement = " + this.sPaiement);
                                        //penser à inverser les flux si erreur de création
                                        this.oosPaiement = new ObjectOutputStream(this.sPaiement.getOutputStream());
                                        this.oisPaiement = new ObjectInputStream(this.sPaiement.getInputStream());

                                        System.out.println("oisPaiement = " + this.oisPaiement);
                                        System.out.println("oosPaiement = " + this.oosPaiement);

                                        oosPaiement.writeObject("SERVEURRESA");

                                        String confirmation;
                                        confirmation = (String) oisPaiement.readObject();
                                        if(confirmation.equals("OK")) {
                                            System.out.println("Connexion réussie");
                                            //requete de paiement
                                            oosPaiement.writeObject("PROOMPAY");
                                            //envoie id
                                            oosPaiement.writeObject(id);
                                            String confirmationPaiement = (String) oisPaiement.readObject();
                                            System.out.println("Paiement :" + confirmationPaiement);

                                            if(confirmationPaiement.equals("OK")) {
                                                oos.writeObject(confirmationPaiement);
                                            }
                                            else {
                                                oos.writeObject(confirmationPaiement);
                                            }
                                        }
                                        else {
                                            System.out.println("Connexion refusée");
                                        }
                                        oosPaiement.writeObject("Exit");
                                        oosPaiement.close();
                                        oisPaiement.close();
                                        sPaiement.close();
                                    }
                                    break;

                                case "CROOM" :
                                    //RECUPERATION DE L'ID DE LA RESERVATION
                                    String id = (String) ois.readObject();
                                    //INITIALISATION DES VALEURS POUR RECHERCHER DANS LA BD LES RESERVATIONS DU CLIENTS
                                    BR.setTable("ReserActCha");
                                    BR.setCondition("id = " + id);
                                    //SUPPRESSION DANS LA BD DE L'ID CORRESPONDANT
                                    int confirmation = BR.delete();
                                    System.out.println("confirmation delete = " + confirmation);
                                    if(confirmation==1) {
                                        oos.writeObject("OK");
                                    }
                                    else {
                                        oos.writeObject("NOK");
                                    }
                                    break;

                                case "LROOMS" :
                                    Date dt = new Date();

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                    String currentTime = sdf.format(dt);
                                    System.out.println(currentTime);
                                    //RECHERCHE DE TOUTES LES RESERVATIONS A PARTIR D'AUJOURD'HUI
                                    ResultSet resultat = BR.RequestLROOMS(currentTime);

                                    //CREATION DU MESSAGE SOUS FORME DE STRING A ENVOYER AU CLIENT
                                    while (resultat.next()) {
                                        ReserActCha reservation = new ReserActCha();
                                        reservation.set_id(resultat.getInt("id"));
                                        reservation.set_numChambre(resultat.getInt("numChambre"));
                                        reservation.set_persRef(resultat.getString("PersRef"));
                                        reservation.set_date(resultat.getString("dateDeb"));
                                        reservation.set_paye(resultat.getBoolean("paye"));

                                        oos.writeObject(reservation);
                                    }
                                    oos.writeObject(null);
                                    break;

                                case "PROOMWEB" :
                                    InetAddress IP = InetAddress.getByName("localhost");
                                    sPaiement = new Socket(IP, 7000);
                                    System.out.println("sPaiement = " + sPaiement);
                                    System.out.println("Connexion au ServeurPaiement avec le ServeurReservation");

                                    String ID = (String) ois.readObject();
                                    System.out.println("id = " + ID);
                                    System.out.println("sPaiement = " + this.sPaiement);
                                    //penser à inverser les flux si erreur de création
                                    this.oosPaiement = new ObjectOutputStream(this.sPaiement.getOutputStream());
                                    this.oisPaiement = new ObjectInputStream(this.sPaiement.getInputStream());

                                    System.out.println("oisPaiement = " + this.oisPaiement);
                                    System.out.println("oosPaiement = " + this.oosPaiement);

                                    oosPaiement.writeObject("SERVEURRESA");

                                    String confir;
                                    confir = (String) oisPaiement.readObject();
                                    if(confir.equals("OK")) {
                                        System.out.println("Connexion réussie");
                                        //requete de paiement
                                        oosPaiement.writeObject("PROOMPAY");
                                        //envoie id
                                        oosPaiement.writeObject(ID);
                                        String confirmationPaiement = (String) oisPaiement.readObject();
                                        System.out.println("Paiement :" + confirmationPaiement);

                                        if(confirmationPaiement.equals("OK")) {
                                            oos.writeObject(confirmationPaiement);
                                        }
                                        else {
                                            oos.writeObject(confirmationPaiement);
                                        }
                                    }
                                    else {
                                        System.out.println("Connexion refusée");
                                    }
                                    oosPaiement.writeObject("Exit");
                                    oosPaiement.close();
                                    oisPaiement.close();
                                    sPaiement.close();

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
            }
            catch (IOException | SQLException | ClassNotFoundException | ParseException e) {
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
    /*private void stop() {
        this.stop();
    }*/
}
