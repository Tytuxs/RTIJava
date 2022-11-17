package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class ClientReservation {
    public static void main(String[] args) {
        try
        {
            Scanner scn = new Scanner(System.in);

            InetAddress ip = InetAddress.getByName("localhost");

            Socket s = new Socket(ip, 5056);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            int connexion = 1;

            while(connexion == 1) {

                System.out.println("1)se connecter\n" + "2)quitter");
                String login = scn.nextLine();

                //REQUETE LOGIN
                if (login.equals("1")) {
                    dos.writeUTF("LOGIN");
                    System.out.println("user : ");
                    String user = scn.nextLine();
                    System.out.println("password : ");
                    String password = scn.nextLine();
                    dos.writeUTF(user);
                    dos.writeUTF(password);

                    String reponse = dis.readUTF();
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OK")) {
                        int continuer = 1;
                        while (continuer == 1) {
                            System.out.println("Que voulez-vous faire ?\n" + "1)BROOM\n" + "2)PROOM\n" + "3)CROOM\n" + "4)LROOMS\n" + "5)Deconnexion");
                            String tosend = scn.nextLine();

                            if (tosend.equals("Exit")) {
                                dos.writeUTF("Exit");
                                System.out.println("fermeture de la connexion : " + s);
                                s.close();
                                System.out.println("Connexion fermee");
                                break;
                            }

                            if(tosend.equals("5") || tosend.equals("Deconnexion")) {
                                dos.writeUTF("LOGOUT");
                                System.out.println(dis.readUTF());
                                continuer = 0;
                            }

                            String message;

                            switch (tosend) {
                                case "BROOM" :
                                    //REQUETE BROOM
                                    dos.writeUTF("BROOM");
                                    //DEMANDE DES INFOS AU CLIENT
                                    System.out.println("Motel ou Village");
                                    String MouV = scn.nextLine();
                                    System.out.println("Simple, Double ou Familiale(4pers) ?");
                                    String typeChambre = scn.nextLine();
                                    System.out.println("nombre de nuits : ");
                                    String nbNuits = scn.nextLine();
                                    System.out.println("date d'arrivee(yyyy-MM-dd) : ");
                                    String date = scn.nextLine();
                                    System.out.println("Votre nom");
                                    String nom = scn.nextLine();
                                    //ENVOI DES INFOS AUX CLIENTS SOUS FORME DE STRING
                                    dos.writeUTF(MouV);
                                    dos.writeUTF(typeChambre);
                                    dos.writeUTF(nbNuits);
                                    dos.writeUTF(date);
                                    dos.writeUTF(nom);

                                    //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                                    while (true) {
                                        message = dis.readUTF();
                                        if(message.equals("FIN"))
                                            break;
                                        else {
                                            StringTokenizer st = new StringTokenizer(message,";");
                                            while (st.hasMoreTokens()) {
                                                    System.out.println("numero de la chambre : " + st.nextToken());
                                                    System.out.println("prix : " + st.nextToken());
                                            }
                                        }
                                    }
                                    //DEMANDE AU CLIENT LA CHAMBRE QU'IL SOUHAITE ET AUSSI LE PRIX CAR PAS ENREGISTRE TANT QUE PAS D'INTERFACE
                                    System.out.println("numero chambre selectionnee : ");
                                    String choix = scn.nextLine();
                                    if(!choix.equals("Aucune")) {
                                        System.out.println("prix : ");
                                        String prix = scn.nextLine();
                                        dos.writeUTF(choix + ";" + prix + ";");
                                        String confirmation = dis.readUTF();

                                        if (confirmation.equals("OK")) {
                                            System.out.println("Reservation prix en compte");
                                        } else {
                                            System.out.println("Erreur Reservation");
                                        }
                                    }
                                    else {
                                        dos.writeUTF("Aucune;");
                                    }

                                    break;

                                case "PROOM" :
                                    //REQUETE PROOM
                                    dos.writeUTF("PROOM");
                                    System.out.println("Nom du client referent : ");
                                    String nomClient = scn.nextLine();
                                    dos.writeUTF(nomClient);

                                    //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                                    while (true) {
                                        message = dis.readUTF();
                                        if(message.equals("FIN"))
                                            break;
                                        else {
                                            StringTokenizer st = new StringTokenizer(message,";");
                                            while (st.hasMoreTokens()) {
                                                System.out.println("id de la reservation : " + st.nextToken());
                                                System.out.println("numero de la chambre : " + st.nextToken());
                                                System.out.println("prix de la chambre : " + st.nextToken());
                                                System.out.println("Personne referent : " + st.nextToken());
                                                System.out.println("paye : " + st.nextToken());
                                            }
                                        }
                                    }

                                    //DEMANDE AU CLIENT DE PAYER UNE DE LEUR RESERVATION
                                    System.out.println("Voulez-vous payer ?\n1)Oui\n2)Non");
                                    String paye = scn.nextLine();

                                    if(paye.equals("Oui") || paye.equals("1")) {
                                        dos.writeUTF("OK");
                                        System.out.println("id de la reservation : ");
                                        String id = scn.nextLine();
                                        dos.writeUTF(id);
                                        String confirmation = dis.readUTF();
                                        if (confirmation.equals("OK")) {
                                            System.out.println("Paiement Accepte");
                                        } else {
                                            System.out.println("Erreur paiement");
                                        }
                                    }
                                    else {
                                        dos.writeUTF("NOK");
                                    }
                                    break;

                                case "CROOM" :
                                    //REQUETE CROOM
                                    dos.writeUTF("CROOM");
                                    //DEMANDE AU CLIENT L'ID POUR SUPPRIMER LA RESEVATION CORRESPONDANTE
                                    System.out.println("id de la reservation : ");
                                    String id = scn.nextLine();
                                    dos.writeUTF(id);
                                    System.out.println("message retour delete : " + dis.readUTF());

                                    break;

                                case "LROOMS" :
                                    //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                                    dos.writeUTF("LROOMS");
                                    //
                                    while (true) {
                                        message = dis.readUTF();
                                        System.out.println(message);
                                        if(message.equals("FIN"))
                                            break;
                                    }
                                    break;
                                default :
                                    break;
                            }
                        }
                    } else {
                        System.out.println("User ou password incorrect");
                    }
                } else {
                    connexion = 0;
                    System.out.println("Vous quittez");
                    dos.writeUTF("Exit");
                }
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}