package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ClientActivite {
    public static void main(String[] args) {
        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String currentTime = sdf.format(dt);
        System.out.println(currentTime);

        try
        {
            Scanner scn = new Scanner(System.in);

            InetAddress ip = InetAddress.getByName("localhost");

            Socket s = new Socket(ip, 6000);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            int connexion = 1;

            while(connexion == 1) {

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
                            System.out.println("Que voulez-vous faire ?\n" + "1)SHACT\n" + "2)LGACT\n" + "3)LACT\n" + "4)DELACT\n" + "5)Deconnexion");
                            String tosend = scn.nextLine();
                            dos.writeUTF(tosend);

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

            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
