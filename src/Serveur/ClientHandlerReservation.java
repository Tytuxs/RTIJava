package Serveur;

import Classe.Chambre;
import Classe.ReserActCha;
import Classe.SourceTaches;
import Classe.Utilisateur;
import database.facility.BD_Bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    BD_Bean BR;

    // Constructor
    /*public ClientHandlerReservation(Socket s, ObjectInputStream ois, ObjectOutputStream oos) throws SQLException {
        this.s = s;
        this.ois = ois;
        this.oos = oos;
        BR = new BeanReservation("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
    }*/

    public ClientHandlerReservation(SourceTaches tachesAFaire, BD_Bean BR) {
        this.tachesAExecuter = tachesAFaire;
        this.BR = BR;
    }

    @Override
    public synchronized void run()
    {
        String received;
        int connexion = 1;
        while (connexion == 1)
        {
            try {
                //attends de recevoir un client
                tacheEnCours = tachesAExecuter.getTache();
            } catch (InterruptedException e) {
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
                    System.out.println("Connection fermée");
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
                                        int confirmation = BR.Insert();

                                        if(confirmation == 1) {
                                            oos.writeObject("OK");
                                        }
                                        else {
                                            oos.writeObject("NOK");
                                        }
                                    }

                                    break;

                                case "PROOM" :
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
                                    if(paye.equals("OK")) {
                                        System.out.println("IciOK");
                                        String id = (String) ois.readObject();
                                        BR.setTable("ReserActCha");
                                        BR.setCondition("id = " + id);
                                        BR.setValues("paye = true");
                                        int confirmation = BR.Update();
                                        if(confirmation==1) {
                                            oos.writeObject("OK");
                                        }
                                        else {
                                            oos.writeObject("NOK");
                                        }
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
            } catch (IOException | SQLException | ClassNotFoundException | ParseException e) {
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
