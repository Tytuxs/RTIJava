package Serveur;

import Classe.*;
import database.facility.BD_Bean;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerActivite extends Thread {

    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    BD_Bean BA;

    public ClientHandlerActivite(SourceTaches tachesAFaire, BD_Bean BA) {
        this.tachesAExecuter = tachesAFaire;
        this.BA = BA;
    }

    //supprimer synchronized ??
    @Override
    public void run() {
        String received;
        int connexion = 1;
        while (connexion == 1)
        {
            try {
                tacheEnCours = tachesAExecuter.getTache();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ACTIVITE : Boucle connexion");
            try {

                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                received = (String) ois.readObject();

                if(received.equals("Exit"))
                {
                    System.out.println("ACTIVITE : Client " + this.tacheEnCours + " quitte...");
                    System.out.println("ACTIVITE : Fermeture connexion.");
                    System.out.println("ACTIVITE : Connexion fermée");
                    connexion = 0;
                }

                if(received.equals("LOGIN")) {
                    Utilisateur user = (Utilisateur) ois.readObject();

                    ResultSet rs = this.BA.Login();
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

                    if(ok==1) {
                        oos.writeObject("OK");
                        int continuer = 1;
                        while (continuer == 1) {
                            BA.setValues("");
                            BA.setTable("");
                            BA.setCondition("");
                            BA.setColumns("");
                            System.out.println("ACTIVITE : Boucle continuer");
                            String requete = (String) ois.readObject();
                            switch (requete) {

                                case "SHACT" :
                                    //on recoit nbJour pour savoir si c'est une courte ou longue activite
                                    int nbJour = (int) ois.readObject();

                                    //différents set pour faire la requete
                                    if(nbJour==0) {
                                        BA.setCondition("nbjour = 1");
                                    }
                                    else {
                                        BA.setCondition("nbjour > 1");
                                    }

                                    BA.setTable("Activite");
                                    //recuperation des resultats
                                    ResultSet resultatSHACT = BA.Select(false);

                                    while (resultatSHACT.next()) {
                                        //envoi chaque objet activite correspondant a la demande
                                        Activite activite = new Activite();
                                        activite.setId(resultatSHACT.getInt("id"));
                                        activite.setType(resultatSHACT.getString("type"));
                                        activite.setDate(resultatSHACT.getString("datedeb"));
                                        activite.setNbJours(resultatSHACT.getInt("nbjour"));
                                        activite.setDureeHeure(resultatSHACT.getInt("dureeheure"));
                                        activite.setNbMaxParticipants(resultatSHACT.getInt("nbmaxparticipants"));
                                        activite.setNbInscrits(resultatSHACT.getInt("nbinscrits"));
                                        activite.setPrixHTVA(resultatSHACT.getFloat("prixHTVA"));
                                        oos.writeObject(activite);
                                    }
                                    //envoit un objet null pour dire que c'est fini
                                    oos.writeObject(null);

                                    String message = (String) ois.readObject();
                                    if(message.equals("Exit")) {
                                        System.out.println("quitter");
                                    }
                                    else {
                                        ReserActCha reservation = (ReserActCha) ois.readObject();

                                        //INITIALISATION DES VALEURS A METTRE DANS LA BD
                                        BA.setTable("reseractcha");
                                        BA.setColumns("`PersRef`,`type`,`typeAct`,`nbMaxAct`,`nbInscrits`,`nbJour`,`dateDeb`,`dureeHeure`,`prixAct`,`paye`,`idAct`");
                                        BA.setValues("'"+reservation.get_persRef()+"'"+", '"
                                                +reservation.get_type()+"', '"
                                                +reservation.get_typeAct()+"', "
                                                +reservation.get_nbMaxAct()+", "
                                                +reservation.get_nbInscrit()+", "
                                                +reservation.get_nbJour()+", '"
                                                +reservation.get_date()+"', "
                                                +reservation.get_dureeHeure()+", "
                                                +reservation.get_prixAct()+", "
                                                +false+", "
                                                +reservation.get_idAct());
                                        //AJOUT A LA BD
                                        int confirmation = BA.Insert();
                                        if(confirmation == 1) {
                                            //si la reservation s'ajoute bien, on doit donc mettre a jour le nombre d'inscrit dans la table activite
                                            oos.writeObject("OK");
                                            BA.setTable("activite");
                                            BA.setValues("nbInscrits = nbInscrits + " + reservation.get_nbInscrit());
                                            BA.setCondition("id = " + reservation.get_idAct());
                                            BA.Update();
                                        }
                                        else {
                                            oos.writeObject("NOK");
                                        }
                                    }
                                    break;

                                case "LISTACT" :
                                    //recuperation nom activite et set de la requete
                                    String nomActivite = (String) ois.readObject();
                                    BA.setTable("Activite");
                                    BA.setCondition("type = '" + nomActivite + "'");
                                    ResultSet resultatLISTACT = BA.Select(false);

                                    while (resultatLISTACT.next()) {
                                        //envoi chaque objet activite correspondant a la demande
                                        Activite activite = new Activite();
                                        activite.setId(resultatLISTACT.getInt("id"));
                                        activite.setType(resultatLISTACT.getString("type"));
                                        activite.setDate(resultatLISTACT.getString("datedeb"));
                                        activite.setNbJours(resultatLISTACT.getInt("nbjour"));
                                        activite.setNbMaxParticipants(resultatLISTACT.getInt("nbmaxparticipants"));
                                        activite.setNbInscrits(resultatLISTACT.getInt("nbinscrits"));
                                        oos.writeObject(activite);
                                    }
                                    //envoit un objet null pour dire que c'est fini
                                    oos.writeObject(null);

                                    //recuperation nom activite et set de la requete
                                    int IDActivite = (int) ois.readObject();
                                    BA.setTable("reseractcha");
                                    BA.setCondition("idAct = " + IDActivite);
                                    ResultSet resultatParticipants = BA.Select(false);

                                    while (resultatParticipants.next()) {
                                        //envoi chaque reservation a l'activite correspondante
                                        ReserActCha reservation = new ReserActCha();
                                        reservation.set_id(resultatParticipants.getInt("id"));
                                        reservation.set_typeAct(resultatParticipants.getString("typeAct"));
                                        reservation.set_nbInscrit(resultatParticipants.getInt("nbinscrits"));
                                        reservation.set_persRef(resultatParticipants.getString("persref"));
                                        oos.writeObject(reservation);
                                    }
                                    oos.writeObject(null);

                                    //attends que le client quitte la fenetre, messageretour = "Exit"
                                    String messageretour = (String) ois.readObject();
                                    System.out.println("messageretour = " + messageretour);

                                    break;

                                case "DELACT" :
                                    String id = (String) ois.readObject();
                                    //INITIALISATION DES VALEURS POUR RECHERCHER DANS LA BD LES RESERVATIONS DU CLIENTS
                                    BA.setTable("ReserActCha");
                                    BA.setCondition("id = " + id);
                                    ResultSet resultat = BA.Select(false);
                                    int nbInscrit = 0;
                                    int idActivite = 0;
                                    //on prend le nombre d'inscrit par cette réservation pour la retirer dans la table activite
                                    while(resultat.next()) {
                                        nbInscrit = resultat.getInt("nbInscrits");
                                        idActivite = resultat.getInt("idAct");
                                    }
                                    //SUPPRESSION DANS LA BD DE L'ID CORRESPONDANT
                                    int confirmation = BA.delete();
                                    System.out.println("confirmation delete = " + confirmation);
                                    if(confirmation==1) {
                                        //si la reservation se retire bien, on doit donc mettre a jour le nombre d'inscrit dans la table activite
                                        oos.writeObject("OK");
                                        BA.setTable("activite");
                                        BA.setCondition("id = " + idActivite);
                                        BA.setValues("nbInscrits = nbInscrits - " + nbInscrit);
                                        BA.Update();
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
                            }
                        }
                    }
                    else {
                        System.out.println("ACTIVITE : NOK");
                        oos.writeObject("NOK");
                    }
                }
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        try
        {
            System.out.println("ACTIVITE : Fermeture des ressources");
            this.tacheEnCours.close();
            this.ois.close();
            this.oos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
