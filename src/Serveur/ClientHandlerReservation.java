package Serveur;

import database.facility.BeanReservation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerReservation extends Thread {

    final Socket s;
    final DataOutputStream dos;
    final DataInputStream dis;
    BeanReservation BC;

    // Constructor
    public ClientHandlerReservation(Socket s, DataInputStream dis, DataOutputStream dos) throws SQLException {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        BC = new BeanReservation("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
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

                    ResultSet rs = this.BC.Login();

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
                            System.out.println("Boucle continuer");
                            String requete = dis.readUTF();
                            switch (requete) {

                                case "BROOM" :
                                    dos.writeUTF("BROOM");
                                    break;

                                case "PROOM" :
                                    dos.writeUTF("PROOM");
                                    break;

                                case "CROOM" :
                                    dos.writeUTF("CROOM");
                                    break;

                                case "LROOMS" :
                                    dos.writeUTF("LROOMS");
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
            } catch (IOException | SQLException e) {
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
