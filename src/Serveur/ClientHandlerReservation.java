package Serveur;

import database.facility.BD_Bean;
import database.facility.BeanReservation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class ClientHandlerReservation extends Thread {

    final Socket s;
    final DataOutputStream dos;
    final DataInputStream dis;
    BD_Bean BR;

    // Constructor
    public ClientHandlerReservation(Socket s, DataInputStream dis, DataOutputStream dos) throws SQLException {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        BR = new BeanReservation("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
    }

    @Override
    public synchronized void run()
    {
        String received;
        int connexion = 1;
        while (connexion == 1)
        {
            System.out.println("Boucle connexion");
            try {

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    System.out.println("Connection closed");
                    connexion = 0;
                }

                if(received.equals("LOGIN")) {
                    String user = dis.readUTF();
                    String password = dis.readUTF();
                    System.out.println("user = " + user);
                    System.out.println("password = " + password);

                    ResultSet rs = this.BR.Login();

                    System.out.println("resultat");
                    int ok = 0;
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if(user.equals(userbd) && password.equals(pwdbd)) {
                            ok = 1;
                        }
                    }

                    if(ok == 1) {
                        dos.writeUTF("OK");

                        int continuer = 1;
                        while (continuer == 1) {
                            //reset a chaque requete pour eviter les erreurs entre les différentes requete d'un meme lance l'une à la suite de l'autre s
                            BR.setTable("");
                            BR.setColumns("");
                            BR.setValues("");

                            System.out.println("Boucle continuer");
                            //attente de la requete
                            String requete = dis.readUTF();
                            switch (requete) {

                                case "BROOM" :
                                    //recuperation des differents champs demandes
                                    String MouV = dis.readUTF();
                                    String typeChambre = dis.readUTF();
                                    String nbNuits = dis.readUTF();
                                    String date = dis.readUTF();
                                    String nom = dis.readUTF();

                                    //Creation de la date de fin grace a date et nbNuits
                                    SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(temp.parse(date));
                                    c.add(Calendar.DATE,Integer.parseInt(nbNuits) - 1);
                                    String dateFin=temp.format(c.getTime());

                                    //REQUETE A LA BD
                                    ResultSet resultatBROOM = BR.RequestBROOM(MouV,typeChambre,date,dateFin);
                                    //CREATION DU MESSAGE SOUS FORME DE STRING A ENVOYER AU CLIENT
                                    while (resultatBROOM.next()) {
                                        String message = resultatBROOM.getString("numeroChambre") + ";" + resultatBROOM.getString("PrixHTVA") + ";";
                                        System.out.println("message envoye = " + message);
                                        dos.writeUTF(message);
                                    }
                                    dos.writeUTF("FIN");
                                    //ATTENTE DU CHOIX DU CLIENT POUR LA CHAMBRE
                                    String retour = dis.readUTF();
                                    StringTokenizer st = new StringTokenizer(retour,";");
                                    String choix = st.nextToken();

                                    if(!choix.equals("Aucune")) {
                                        String prix = st.nextToken();
                                        int nbMaxCha = 0;
                                        if(typeChambre.equals("Simple")) {
                                            nbMaxCha=1;
                                        }
                                        if(typeChambre.equals("Double")) {
                                            nbMaxCha=2;
                                        }
                                        if(typeChambre.equals("Familiale")) {
                                            nbMaxCha=4;
                                        }
                                        //INITIALISATION DES VALEURS A METTRE DANS LA BD
                                        BR.setTable("reseractcha");
                                        BR.setColumns("`PersRef`,`type`,`numChambre`,`typeCha`,`nbMaxCha`,`nbNuit`,`dateDeb`,`prixCha`,`paye`");
                                        BR.setValues("'"+nom+"'"+","+"'Chambre'"+","+choix+","+"'"+typeChambre+"'"+","+nbMaxCha+","+Integer.parseInt(nbNuits)+","+"'"+date+"'"+","+prix+","+false);
                                        //AJOUT A LA BD
                                        int confirmation = BR.Insert();

                                        if(confirmation == 1) {
                                            dos.writeUTF("OK");
                                        }
                                        else {
                                            dos.writeUTF("NOK");
                                        }
                                    }

                                    break;

                                case "PROOM" :
                                    //RECUPERATION DU NOM DU CLIENT
                                    String nomClient = dis.readUTF();
                                    //INITIALISATION DES VALEURS POUR RECHERCHER DANS LA BD LES RESERVATIONS DU CLIENTS
                                    BR.setTable("ReserActCha");
                                    BR.setCondition("PersRef = '" + nomClient + "' and " + "type = 'Chambre'");
                                    //RECHERCHE DANS LA BD
                                    ResultSet resultatPROOM = BR.Select(false);
                                    //CREATION DU MESSAGE SOUS FORME DE STRING A ENVOYER AU CLIENT
                                    while (resultatPROOM.next()) {
                                        String message = resultatPROOM.getString("id") + ";"
                                                + resultatPROOM.getString("numChambre") + ";"
                                                + resultatPROOM.getString("prixCha") + ";"
                                                + resultatPROOM.getString("PersRef") + ";"
                                                + resultatPROOM.getString("paye");
                                        System.out.println("message envoye = " + message);
                                        dos.writeUTF(message);
                                    }
                                    dos.writeUTF("FIN");
                                    //ATTENTE DU CLIENT POUR PAYER
                                    String paye = dis.readUTF();
                                    if(paye.equals("OK")) {
                                        System.out.println("IciOK");
                                        String id = dis.readUTF();
                                        BR.setTable("ReserActCha");
                                        BR.setCondition("id = " + id);
                                        BR.setValues("paye = true");
                                        int confirmation = BR.Update();
                                        if(confirmation==1) {
                                            dos.writeUTF("OK");
                                        }
                                        else {
                                            dos.writeUTF("NOK");
                                        }
                                    }
                                    break;

                                case "CROOM" :
                                    //RECUPERATION DE L'ID DE LA RESERVATION
                                    String id = dis.readUTF();
                                    //INITIALISATION DES VALEURS POUR RECHERCHER DANS LA BD LES RESERVATIONS DU CLIENTS
                                    BR.setTable("ReserActCha");
                                    BR.setCondition("id = " + id);
                                    //SUPPRESSION DANS LA BD DE L'ID CORRESPONDANT
                                    int confirmation = BR.delete();
                                    System.out.println("confirmation delete = " + confirmation);
                                    if(confirmation==1) {
                                        dos.writeUTF("OK");
                                    }
                                    else {
                                        dos.writeUTF("NOK");
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
                                        String message = resultat.getString("numChambre") + ";" + resultat.getString("PersRef") + ";";
                                        System.out.println("message envoye = " + message);
                                        dos.writeUTF(message);
                                    }
                                    dos.writeUTF("FIN");
                                    break;

                                case "LOGOUT" :
                                    dos.writeUTF("Au revoir");
                                    continuer = 0;
                                    break;

                                case "Exit" :
                                    dos.writeUTF("Au revoir");
                                    continuer = 0;
                                    connexion = 0;
                                    break;

                                default:
                                    dos.writeUTF("ERROR : Invalid input");
                                    break;
                            }
                        }
                    }
                    else {
                        System.out.println("NOK");
                        dos.writeUTF("NOK");
                    }
                }
            } catch (IOException | SQLException | ParseException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.s.close();
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    /*private void stop() {
        this.stop();
    }*/
}
