package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class App_ReservationClient extends JDialog {
    private JButton buttonCROOM;
    private JPanel panelReservation;
    private JButton buttonBROOM;
    private JButton buttonPROOM;
    private JButton buttonLROOMS;
    private JButton buttonQuitter;

    public App_ReservationClient(Socket s, DataOutputStream dos, DataInputStream dis) throws IOException{

            buttonBROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    App_BROOM app_broom = new App_BROOM(s,dos,dis);
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

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // closing resources

                try {
                    dis.close();
                    dos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    this.setMinimumSize(new Dimension(600,600));
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setContentPane(panelReservation);
    this.pack();
    }


}
