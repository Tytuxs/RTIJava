package Windows.Activite;

import Windows.App_CROOM;
import Windows.App_ReservationClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class App_ActiviteClient extends JDialog {
    private JPanel ActivitePanel;
    private JButton activitéCourteButton;
    private JButton activitéLongueButton;
    private JButton annulationDUneActivitéButton;
    private JButton listeDesActivitésButton;
    private JButton quitterButton;

    public App_ActiviteClient(Socket s, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {

        activitéCourteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_SHACT app_shact = new App_SHACT(oos,ois,0);
                app_shact.setVisible(true);
            }
        });

        activitéLongueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_SHACT app_lgact = new App_SHACT(oos,ois,1);
                app_lgact.setVisible(true);
            }
        });

        annulationDUneActivitéButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_CROOM app_delact = new App_CROOM(s, oos, ois, "DELACT");
                app_delact.setVisible(true);
            }
        });

        listeDesActivitésButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_LISTACT app_listact = new App_LISTACT(oos,ois);
                app_listact.setVisible(true);
            }
        });

        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("Exit");
                    s.close();
                    ois.close();
                    oos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                App_ActiviteClient.super.dispose();
            }
        });


        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(ActivitePanel);
        this.pack();
    }
}
