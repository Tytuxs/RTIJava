package Windows;

import Config.DefaultHandlerPerso;
import Serveur.*;

import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class App_ConnexionServer extends JDialog{
    private JPanel serverPanel;
    private JButton button_LancerServeur;
    private JButton annulerButton;
    private DefaultHandlerPerso defaultHandler;

    public App_ConnexionServer() throws SQLException {
        button_LancerServeur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    defaultHandler = new DefaultHandlerPerso();
                    saxParser.parse("./src/Config/server.xml", defaultHandler);
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_CHAMBRE());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_ACTIVITE());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_PAY());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_ADMIN());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_ADMINS());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_URGENCE());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_CARD());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_BANK());
                    System.out.println("PORT_CHAMBRE = " + defaultHandler.getPORT_GROUPE());
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                try {
                    ServeurReservation serveurResaChambre = new ServeurReservation(defaultHandler.getPORT_CHAMBRE());
                    ServeurActivite serveurResaActi = new ServeurActivite(defaultHandler.getPORT_ACTIVITE());
                    ServeurPaiement serveurPaiement = new ServeurPaiement(defaultHandler.getPORT_PAY(), defaultHandler.getPORT_ADMIN(), defaultHandler.getPORT_ADMINS(), defaultHandler.getPORT_URGENCE());
                    ServeurCarte serveurCarte = new ServeurCarte(defaultHandler.getPORT_CARD());
                    ServeurBanque serveurBanque = new ServeurBanque(defaultHandler.getPORT_BANK());
                    ServeurIeyhChat serveurChat = new ServeurIeyhChat(defaultHandler.getPORT_GROUPE());


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

                    Thread threadBanque = new Thread(serveurBanque);
                    System.out.println("Lancement du ServeurBanque avec un thread");
                    threadBanque.start();

                    Thread threadChat = new Thread(serveurChat);
                    System.out.println("Lancement du ServeurChat avec un thread");
                    threadChat.start();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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
