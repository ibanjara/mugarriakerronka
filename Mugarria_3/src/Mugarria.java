import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
public class Mugarria extends JFrame {
    private static final String pasahitza = "damocles";
    private static final String karpeta = "./";
    private JComboBox<String> combo;
    private JLabel argazkia;
    private JCheckBox komentarioaCheckbox;
    private JTextField komentarioaTextua;

    public Mugarria() {
        this.combo = new JComboBox<>();
        this.argazkia = new JLabel();
        this.komentarioaCheckbox = new JCheckBox("Komentarioa gorde",true);
        this.komentarioaTextua = new JTextField("",10);
        setSize(400, 400);

        String input = JOptionPane.showInputDialog(this, "Pasahitza sartu");
        if (!pasahitza.equals(input)) {
            JOptionPane.showMessageDialog(this, "Pasahitza ez da zuzena. Programa ixten...");
            System.exit(0);
        } else{
            setVisible(true);
        }

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel goikoaldea = new JPanel(new FlowLayout(FlowLayout.LEFT));
        goikoaldea.add(this.combo);
        contentPane.add(goikoaldea, BorderLayout.NORTH);

        JPanel erdialdea = new JPanel(new BorderLayout());
        erdialdea.add(this.argazkia, BorderLayout.CENTER);
        contentPane.add(erdialdea, BorderLayout.CENTER);

        JPanel ezkerrekoaldea = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ezkerrekoaldea.add(this.komentarioaCheckbox);

        JPanel eskubialdea = new JPanel(new BorderLayout());
        eskubialdea.add(this.komentarioaTextua, BorderLayout.CENTER);

        JButton gorde = new JButton("GORDE");
        JPanel botoia = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoia.add(gorde);

        JPanel panelOsoa = new JPanel(new BorderLayout()); //panel osoa erdialdea eta goiko aldea kenduta (dagoeneko gehituta)
        panelOsoa.add(ezkerrekoaldea, BorderLayout.WEST);
        panelOsoa.add(botoia, BorderLayout.SOUTH);
        panelOsoa.add(eskubialdea, BorderLayout.CENTER);

        contentPane.add(panelOsoa, BorderLayout.SOUTH);

        try{
            loadCombo();
            this.combo.setSelectedIndex(0);

            ImageIcon argazkia = new ImageIcon(karpeta + "/" + (String) this.combo.getSelectedItem());
            Image argazkiaLortu = argazkia.getImage();
            Image argazkiaEskalatuta = argazkiaLortu.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            ImageIcon argazkiaOna = new ImageIcon(argazkiaEskalatuta);
            this.argazkia.setIcon(argazkiaOna);

        }catch (IllegalArgumentException e) {
            System.out.println("Ez dago argazkirik");
        }

        this.combo.addActionListener(new ComboListener());
        gorde.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String argazkiaIzena = (String) combo.getSelectedItem();
                String textua = komentarioaCheckbox.isSelected() ? komentarioaTextua.getText() : "";
                String fitxategiaIzena = karpeta + "/" + argazkiaIzena + ".txt";

                try (PrintWriter writer = new PrintWriter(new FileWriter(fitxategiaIzena, true))) {
                    writer.println(argazkiaIzena + " " + textua);
                    JOptionPane.showMessageDialog(Mugarria.this, "Komentarioa gordeta!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Mugarria.this, "Errore bat egon da komentarioa idazterakoan: " + ex.getMessage());
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(Mugarria.this, "Agur!");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }

    private void loadCombo() {
        File karpetaa = new File(karpeta);
        if (karpetaa.isDirectory()) {
            for (File fitxategia : karpetaa.listFiles()) {
                if (fitxategia.isFile()) {
                    String izena = fitxategia.getName();
                    if (izena.endsWith(".jpg") || izena.endsWith(".png") || izena.endsWith(".jpeg")) {
                        this.combo.addItem(izena);
                    }
                }
            }
        }
    }

    private class ComboListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String argazkiaIzena = (String) combo.getSelectedItem();
            ImageIcon argazkiaa = new ImageIcon(karpeta + "/" + argazkiaIzena);
            Image argazkiaLortu = argazkiaa.getImage();
            Image argazkiaEskalatuta = argazkiaLortu.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            ImageIcon argazkiaOna = new ImageIcon(argazkiaEskalatuta);
            argazkia.setIcon(argazkiaOna);

            komentarioaTextua.setText("");
        }
    }

    public static void main(String[] args) {
        new Mugarria();
    }
}

