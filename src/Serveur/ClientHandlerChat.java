package Serveur;

import Classe.Chat;
import Classe.SourceTaches;
import Classe.Utilisateur;
import database.facility.BD_Bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerChat extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    BD_Bean BC;

    public ClientHandlerChat(SourceTaches tachesAFaire, BD_Bean BC) {
        this.tachesAExecuter = tachesAFaire;
        this.BC = BC;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheEnCours = tachesAExecuter.getTache();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ServeurChat : Boucle connexion");
            try {
                //creation des flux
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) ois.readObject();
                System.out.println("received = " + received);

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                    System.out.println("Connexion fermée");
                }

                if (received.equals("LOGIN_GROUP")) {
                    Utilisateur user = (Utilisateur) ois.readObject();

                    ResultSet rs = this.BC.Login();
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if (user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                            System.out.println("Client trouve");
                            oos.writeObject("OK");
                            Chat chat = new Chat();
                            chat.setPORT_CHAT(11010);
                            chat.setIp("234.5.5.9");
                            oos.writeObject(chat);
                            break;
                        }
                        else {
                            oos.writeObject("NOK");
                        }
                    }
                }
                this.tacheEnCours.close();
                this.ois.close();
                this.oos.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
