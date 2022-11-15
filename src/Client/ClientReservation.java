package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class ClientReservation {
    public static void main(String[] args) {
        //date marche et normalement se met sous ce format dans mysql aussi
        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String currentTime = sdf.format(dt);
        System.out.println(currentTime);

        try
        {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            int connexion = 1;

            while(connexion == 1) {

                // the following loop performs the exchange of
                // information between client and client handler
                System.out.println("1)se connecter\n" + "2)quitter");
                String login = scn.nextLine();

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
                            dos.writeUTF(tosend);

                            // If client sends exit,close this connection
                            // and then break from the while loop
                            if (tosend.equals("Exit")) {
                                System.out.println("Closing this connection : " + s);
                                s.close();
                                System.out.println("Connection closed");
                                break;
                            }

                            if(tosend.equals("5") || tosend.equals("Deconnexion")) {
                                dos.writeUTF("LOGOUT");
                                System.out.println(dis.readUTF());
                                continuer = 0;
                            }

                            // printing date or time as requested by client
                            String received = dis.readUTF();
                            System.out.println(received);
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