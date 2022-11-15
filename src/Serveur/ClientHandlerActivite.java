package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandlerActivite extends Thread {

    final Socket s;
    final DataOutputStream dos;
    final DataInputStream dis;

    // Constructor
    public ClientHandlerActivite(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public synchronized void run() {
        String received;
        int connexion = 1;
        while (connexion == 1)
        {
            System.out.println("ACTIVITE : Boucle connexion");
            try {

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("ACTIVITE : Client " + this.s + " sends exit...");
                    System.out.println("ACTIVITE : Closing this connection.");
                    System.out.println("ACTIVITE : Connection closed");
                    connexion = 0;
                }

                if(received.equals("LOGIN")) {
                    String user = dis.readUTF();
                    String password = dis.readUTF();
                    System.out.println("user = " + user);
                    System.out.println("password = " + password);

                    if(user.equals("Oli") && password.equals("4653")) {
                        dos.writeUTF("OK");

                        // write on output stream based on the
                        // answer from the client
                        int continuer = 1;
                        while (continuer == 1) {
                            System.out.println("ACTIVITE : Boucle continuer");
                            String requete = dis.readUTF();
                            switch (requete) {

                                case "SHACT" :
                                    dos.writeUTF("SHACT");
                                    break;

                                case "LGACT" :
                                    dos.writeUTF("LGACT");
                                    break;

                                case "LACT" :
                                    dos.writeUTF("LACT");
                                    break;

                                case "DELACT" :
                                    dos.writeUTF("DELACT");
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
                        System.out.println("ACTIVITE : NOK");
                        dos.writeUTF("NOK");
                    }
                }
            } catch (IOException e) {
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
}
