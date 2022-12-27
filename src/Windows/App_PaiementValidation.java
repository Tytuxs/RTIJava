package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class App_PaiementValidation extends JDialog {
    private JPanel panelPaiementValidation;
    private JButton buttonValider;
    private JTextField textFieldCB;
    private JTextField textFieldMDP;
    private JTextField textFieldMontant;

    public App_PaiementValidation(String id, ObjectOutputStream oos, ObjectInputStream ois) {

        buttonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    float paiement = Float.parseFloat(textFieldMontant.getText());
                    oos.writeObject(id);
                    oos.writeObject(paiement);
                    oos.writeObject(textFieldCB.getText());
                    oos.writeObject(textFieldMDP.getText());

                    String confirmation = (String) ois.readObject();
                    if (confirmation.equals("OK")) {
                        JOptionPane.showMessageDialog(null, "Paiement fait", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Paiement déjà fait ou Erreur lors du paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });

        this.setMinimumSize(new Dimension(600, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelPaiementValidation);
        this.pack();
    }
}
