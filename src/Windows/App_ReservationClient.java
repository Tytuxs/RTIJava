package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class App_ReservationClient extends JDialog {
    private JButton buttonCROOM;
    private JPanel panelReservation;
    private JButton buttonBROOM;
    private JButton buttonPROOM;
    private JButton buttonLROOMS;
    private JButton buttonQuitter;

    public App_ReservationClient() throws IOException{

        InetAddress ip = InetAddress.getByName("localhost");
        Socket s = new Socket(ip, 5056);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        int connexion = 1;

        while(connexion == 1) {

            buttonBROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
/*
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

 */
                }
            });

            buttonPROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
/*
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
 */
                }
            });

            buttonCROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                /*    //REQUETE CROOM
                    dos.writeUTF("CROOM");
                    //DEMANDE AU CLIENT L'ID POUR SUPPRIMER LA RESEVATION CORRESPONDANTE
                    System.out.println("id de la reservation : ");
                    String id = scn.nextLine();
                    dos.writeUTF(id);
                    System.out.println("message retour delete : " + dis.readUTF());
*/
                }
            });

            buttonLROOMS.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                 /*   //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                    dos.writeUTF("LROOMS");
                    //
                    while (true) {
                        message = dis.readUTF();
                        System.out.println(message);
                        if(message.equals("FIN"))
                            break;
                    }
*/
                }
            });


        }
    this.setMinimumSize(new Dimension(600,600));
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setContentPane(panelReservation);
    this.pack();
    }


}
