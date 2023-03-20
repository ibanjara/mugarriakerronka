import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Mugarria extends JFrame implements ActionListener {
    private String[] izenak = {"python.txt", "c.txt", "java.txt"};
    private JComboBox<String> comboBox;
    private JTextArea textArea;
    private JButton borrar, itxi;
    private JPanel comboBoxPanel, itxiBotoia;
    private JScrollPane scroll;

    public Mugarria() {

        //COMBOBOX
        this.comboBoxPanel = new JPanel();
        this.borrar = new JButton("Borrar");
        this.comboBoxPanel.setPreferredSize(new Dimension(500, 500));
        this.comboBox = new JComboBox<>(izenak);
        this.comboBox.setPreferredSize(new Dimension(300, 25));
        this.comboBoxPanel.add(comboBox);
        this.comboBoxPanel.add(borrar);

        //TEXTAREA
        this.textArea = new JTextArea();
        this.scroll = new JScrollPane(textArea);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        //ITXI
        this.itxiBotoia = new JPanel();
        this.itxi = new JButton("Cerrar");
        this.itxiBotoia.add(this.itxi);

        //POSIZIOAK
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(this.comboBoxPanel, BorderLayout.WEST);
        getContentPane().add(this.scroll, BorderLayout.CENTER);
        getContentPane().add(this.itxiBotoia, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


        //AKZIOAK - ITXI
        itxi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //AKZIOAK - BORRATU
        this.borrar.addActionListener(this);
        //AKZIOAK - IRAKURRI
        this.comboBox.addActionListener(this);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(comboBox)) {
            String aukera = (String) comboBox.getSelectedItem();
            File fitxategia = new File(aukera);

            if (fitxategia.exists()) {
                try {
                    BufferedReader irakurri = new BufferedReader(new FileReader(fitxategia));
                    String irakurketa = irakurri.readLine();
                    String textua = "";
                    textArea.setText("");
                    while (irakurketa != null) {
                        textua += irakurketa + "\n";
                        irakurketa = irakurri.readLine();
                    }
                    irakurri.close();
                    textArea.setText(textua);
                } catch (IOException ex) {
                    System.out.println("Errore bat egon da fitxategia irakurtzeko edo idatezko orduan.");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Fitxategia ez da existizen!");
            }
        }
        if(e.getSource().equals(borrar)){
            textArea.setText("");
        }
    }
}