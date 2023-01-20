package Serveur;

import Classe.Utilisateur;
import database.facility.BD_Bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandlerAdmin extends Thread {

    private ServerSocket ssAdmin;
    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private BD_Bean BP;
    private ArrayList<ClientHandlerUrgence> listThreadClient;
    private ArrayList<Socket> listSocketClient;

    public ClientHandlerAdmin(ServerSocket ss, ArrayList<ClientHandlerUrgence> liste, BD_Bean BP, ArrayList<Socket> listS) {
        this.ssAdmin = ss;
        this.listThreadClient = liste;
        this.BP = BP;
        this.listSocketClient=listS;
    }

    @Override
    public void run() {
        while (true) {
            try {
                s = ssAdmin.accept();
                System.out.println("ADMIN ACCEPTE");
                oos = new ObjectOutputStream(s.getOutputStream());
                ois = new ObjectInputStream(s.getInputStream());

                int connexion = 1;
                while (connexion == 1) {
                    String received = (String) ois.readObject();
                    System.out.println("received = " + received);

                    if (received.equals("Exit")) {
                        System.out.println("ADMIN " + this.s + " quitte...");
                        System.out.println("Fermeture connexion.");
                        System.out.println("Connexion fermée");
                        connexion = 0;
                    }

                    int ok = 0;
                    if (received.equals("LOGINA")) {
                        Utilisateur user = (Utilisateur) ois.readObject();

                        ResultSet rs = this.BP.Login();
                        while (rs.next()) {
                            String userbd = rs.getString(2);
                            String pwdbd = rs.getString(3);

                            if (user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                                System.out.println("Client trouve");
                                ok = 1;
                                break;
                            }
                        }
                    }

                    if (ok == 1) {
                        oos.writeObject("OKADMIN");
                        int continuer = 1;
                        while (continuer == 1) {
                            System.out.println("Boucle continuer");
                            String requete = (String) ois.readObject();
                            System.out.println("requete = " + requete);

                            switch (requete) {
                                case "LCLIENTS":
                                    //oosReservation.writeObject("Au revoir");
                                    ArrayList<Integer> listNumSocketASupp = new ArrayList<Integer>();
                                    for (int i = 0; i < listSocketClient.size(); i++) {
                                        if (listSocketClient.get(i).isClosed())
                                            listNumSocketASupp.add(i);
                                        else {
                                            String ip = listSocketClient.get(i).getInetAddress().getHostAddress();
                                            System.out.println("ip = " + ip);
                                            oos.writeObject(ip);
                                        }
                                    }
                                    oos.writeObject(null);
                                    //on supprime les socket fermees qui n'ont pas ete affichee
                                    for (int i = 0; i < listNumSocketASupp.size(); i++) {
                                        if (listNumSocketASupp.get(i) != null)
                                            listSocketClient.remove(listNumSocketASupp.get(i));
                                    }
                                    break;

                                case "PAUSE":
                                    //oosReservation.writeObject("Au revoir");
                                    for (int i = 0; i < listThreadClient.size(); i++) {
                                        if (listThreadClient.get(i).getConnecte() == 1)
                                            listThreadClient.get(i).sendMessage("Serveur est en pause");

                                        listThreadClient.get(i).interrupt();
                                    }
                                    oos.writeObject("Clients prévenus pour la pause du serveur");
                                    break;

                                case "STOP":
                                    //oosReservation.writeObject("Au revoir");
                                    for (int i = 0; i < listThreadClient.size(); i++) {
                                        listThreadClient.get(i).sendMessage("Serveur est DOWN");
                                        listThreadClient.get(i).interrupt();
                                    }
                                    oos.writeObject("Clients prévenus pouyr l'arrêt du serveur");
                                    break;

                                case "Exit":
                                    //oosReservation.writeObject("Au revoir");
                                    continuer = 0;
                                    connexion = 0;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("NOK");
                        oos.writeObject("NOK");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Fermeture des ressources");
            try {
                this.s.close();
                this.ois.close();
                this.oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
