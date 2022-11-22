package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class App_ReservationClient extends JDialog {
    private JButton buttonCROOM;
    private JPanel panelReservation;
    private JButton buttonBROOM;
    private JButton buttonPROOM;
    private JButton buttonLROOMS;
    private JButton buttonQuitter;

    /*public App_ReservationClient(Socket s, DataOutputStream dos, DataInputStream dis) throws IOException{

            buttonBROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App_BROOM app_broom = new App_BROOM(s,dos,dis);
                    app_broom.setVisible(true);
                }
            });

            buttonPROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App_PROOM app_proom = new App_PROOM(s,dos,dis);
                    app_proom.setVisible(true);
                }
            });

            buttonCROOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App_CROOM app_croom = new App_CROOM(s,dos,dis);
                    app_croom.setVisible(true);
                }
            });

            buttonLROOMS.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App_LROOMS app_lrooms = null;
                    try {
                        app_lrooms = new App_LROOMS(s,dos,dis);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    app_lrooms.setVisible(true);
                }
            });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // closing resources

                try {
                    dos.writeUTF("Exit");
                    s.close();
                    dis.close();
                    dos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                App_ReservationClient.super.dispose();
            }
        });
    this.setMinimumSize(new Dimension(600,600));
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setContentPane(panelReservation);
    this.pack();
    }*/

    public App_ReservationClient(Socket s, ObjectOutputStream oos, ObjectInputStream ois) throws IOException{

        buttonBROOM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                App_BROOM app_broom = new App_BROOM(s,oos,ois);
                app_broom.setVisible(true);
            }
        });

        buttonPROOM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_PROOM app_proom = new App_PROOM(s,oos,ois);
                app_proom.setVisible(true);
            }
        });

        buttonCROOM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_CROOM app_croom = new App_CROOM(s,oos,ois,"CROOM");
                app_croom.setVisible(true);
            }
        });

        buttonLROOMS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_LROOMS app_lrooms = null;
                try {
                    app_lrooms = new App_LROOMS(s,oos,ois);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                app_lrooms.setVisible(true);
            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // closing resources

                try {
                    oos.writeObject("Exit");
                    s.close();
                    ois.close();
                    oos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                App_ReservationClient.super.dispose();
            }
        });
        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelReservation);
        this.pack();
    }


}
