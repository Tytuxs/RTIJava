package Windows;

import Serveur.ServeurActivite;
import Serveur.ServeurCarte;
import Serveur.ServeurPaiement;
import Serveur.ServeurReservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class App_ConnexionServer extends JDialog{
    private JPanel serverPanel;
    private JButton button_LancerServeur;
    private JButton annulerButton;

    private ServeurReservation serveurResaChambre = new ServeurReservation(5056);
    private ServeurActivite serveurResaActi = new ServeurActivite(6000);
    private ServeurPaiement serveurPaiement = new ServeurPaiement(7000);
    private ServeurCarte serveurCarte = new ServeurCarte(8000);

    public App_ConnexionServer() throws SQLException {
        button_LancerServeur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread threadResaChambre = new Thread(serveurResaChambre);
                System.out.println("Lancement du ServeurReservation avec un thread");
                threadResaChambre.start();

                Thread threadResaActi = new Thread(serveurResaActi);
                System.out.println("Lancement du ServeurActivite avec un thread");
                threadResaActi.start();

                Thread threadPaiement = new Thread(serveurPaiement);
                System.out.println("Lancement du ServeurPaiement avec un thread");
                threadPaiement.start();

                Thread threadCarte = new Thread(serveurCarte);
                System.out.println("Lancement du ServeurCarte avec un thread");
                threadCarte.start();
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_ConnexionServer.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(serverPanel);
        this.pack();
    }
}
