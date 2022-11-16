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

                        // write on output stream based on the
                        // answer from the client
                        int continuer = 1;
                        while (continuer == 1) {
                            BR.setTable("");
                            BR.setColumns("");
                            BR.setValues("");

                            System.out.println("Boucle continuer");
                            String requete = dis.readUTF();
                            switch (requete) {

                                case "BROOM" :
                                    String MouV = dis.readUTF();
                                    String typeChambre = dis.readUTF();
                                    String nbNuits = dis.readUTF();
                                    String date = dis.readUTF();
                                    String nom = dis.readUTF();

                                    SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(temp.parse(date));
                                    c.add(Calendar.DATE,Integer.parseInt(nbNuits) - 1);
                                    String dateFin=temp.format(c.getTime());
                                    System.out.println("date arrivee = " + date);
                                    System.out.println("Date fin = " + dateFin);
                                    System.out.println("nombre nuits = " + Integer.parseInt(nbNuits));

                                    ResultSet resultatBROOM = BR.RequestBROOM(MouV,typeChambre,date,dateFin);
                                    while (resultatBROOM.next()) {
                                        String message = resultatBROOM.getString("numeroChambre") + ";" + resultatBROOM.getString("PrixHTVA") + ";";
                                        System.out.println("message envoye = " + message);
                                        dos.writeUTF(message);
                                    }
                                    dos.writeUTF("FIN");
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
                                        BR.setTable("reseractcha");
                                        BR.setColumns("`PersRef`,`type`,`numChambre`,`typeCha`,`nbMaxCha`,`nbNuit`,`dateDeb`,`prixCha`,`paye`");
                                        BR.setValues("'"+nom+"'"+","+"'Chambre'"+","+choix+","+"'"+typeChambre+"'"+","+nbMaxCha+","+Integer.parseInt(nbNuits)+","+"'"+date+"'"+","+prix+","+false);
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
                                    String nomClient = dis.readUTF();
                                    BR.setTable("ReserActCha");
                                    BR.setCondition("PersRef = '" + nomClient + "' and " + "type = 'Chambre'");
                                    ResultSet resultatPROOM = BR.Select(false);
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
                                    String id = dis.readUTF();
                                    BR.setTable("ReserActCha");
                                    BR.setCondition("id = " + id);
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
                                    ResultSet resultat = BR.RequestLROOMS(currentTime);

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
