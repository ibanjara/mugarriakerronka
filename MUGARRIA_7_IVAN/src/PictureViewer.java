import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//DATARENA FROGATZEKO 04/28/2023 AUKERATU
public class PictureViewer extends JFrame {
    private JPanel goianEzkerra, goianEskubia;
    private JLabel photographerLabel, dateLabel, argazkia;
    private JComboBox photographers;
    private JXDatePicker data;
    private JList<Picture> lista;
    private JButton awarded, remove;

    public PictureViewer(){
        setTitle("Mugarria 7 - Ivan Jara");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000,600));

        //gridlayout
        this.setLayout(new GridLayout(3,3,20,20));

        //botoiak
        this.awarded = new JButton("AWARDED");
        this.remove = new JButton("REMOVE");


        //JLabel
        this.photographerLabel = new JLabel("Photographer: ");
        this.dateLabel = new JLabel("Photos after");
        this.argazkia = new JLabel();

        //combobox
        this.photographers = new JComboBox<>(new DefaultComboBoxModel<>(DBkonexioa.loadPhotographers().toArray(new Photographer[]{})));

        //lista
        this.lista = new JList<>();

        //fotografoa aukeratzerakoan argazkiak kargatu.
        photographers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImages();
            }
        });

        //Listako argazkian bi klik egiterakoan argazki hori erakutsi eta +1 bisita gehitu argazki horri Horretarako MouseListener-a.
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Picture selectedImage = lista.getSelectedValue();
                    ImageIcon imageIcon = new ImageIcon(selectedImage.getFile());
                    Image image = imageIcon.getImage().getScaledInstance(250,250,Image.SCALE_SMOOTH);
                    ImageIcon scaledImageIcon = new ImageIcon(image);
                    argazkia.setIcon(scaledImageIcon);
                    selectedImage.incrementVisits();
                }
            }
        });


        this.awarded.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Bisita kopuru minimoa eskatu
                String minimoa = JOptionPane.showInputDialog("Bisita kopurua minimoa sarituta izateko:");
                //HashMap bat sortu DBkonexioa klasean sortu dugun metodoarekin.
                HashMap<Integer,Integer> bisitak = DBkonexioa.createVisitsMap();

                //HashMap-a errekorritu, HashMap-aren key-a lortu (id fotografoa dela) eta id fotografo horrekin bisita kopurua lortu.
                for (int photographerId : bisitak.keySet()) {
                    int bisitaKop = bisitak.get(photographerId);

                    //Bisita kopurua sartutako bisita kopurua berdina edo handiagoa bada, fotografoa saritu.
                    if(bisitaKop >= Integer.parseInt(minimoa)){
                        String query = "UPDATE photographers SET Awarded = true WHERE PhotographerId = " + photographerId;
                        try (Statement statement = DBkonexioa.getConnection().createStatement()) {
                            statement.executeUpdate(query);
                            JOptionPane.showMessageDialog(null,photographerId + " ID-a duen fotografoa saritu egin da!");
                        } catch (SQLException ex) {
                            System.out.println("Errore bat egon da fotografoa saritzerako orduan: " + ex);;
                        }
                    }
                }
            }
        });

        this.remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Aukeratu argazkiak non fotografoa ez dagoen sarituta eta argazkiak 0 bisita duen.
                String query = "SELECT p.PictureId, p.Title, ph.PhotographerId FROM Pictures p INNER JOIN Photographers ph ON p.PhotographerId = ph.PhotographerId WHERE p.Visits = 0 AND ph.Awarded = false";

                try (Statement statement = DBkonexioa.getConnection().createStatement();
                     ResultSet resultSet = statement.executeQuery(query)){

                    while(resultSet.next()){
                        int pictureId = resultSet.getInt("PictureId");
                        String titulua = resultSet.getString("Title");

                       //Argazkia aurkitu bada, galdetu ea borratu nahi duen
                        int aukera = JOptionPane.showConfirmDialog(null, "Argazki hau borratu nahi duzu? " + titulua + " | ID " + pictureId);
                        if (aukera == JOptionPane.YES_OPTION) {
                           String delete = "DELETE FROM Pictures WHERE PictureId = " + pictureId;
                           try (Statement statement2 = DBkonexioa.getConnection().createStatement()){
                               statement2.executeUpdate(delete);
                               JOptionPane.showMessageDialog(null, "Argazkia borratu da.");
                           }
                       }
                   }
               } catch (SQLException ex) {
                   System.out.println("Errore bat egon da argazkia kentzerako orduan: " + ex);
               }

                //Argazkirik ez dituzten fotografoak aukeratu.
               String fotografoak = "SELECT PhotographerId FROM Photographers WHERE PhotographerId NOT IN (SELECT DISTINCT PhotographerId FROM Pictures)";
                try (Statement statement = DBkonexioa.getConnection().createStatement();
                     ResultSet resultSet = statement.executeQuery(fotografoak)){
                    //Aurkitzen bada, fotografoa borratu, argazkirik ez dituelako.
                    while(resultSet.next()){
                        int photographerId = resultSet.getInt("PhotographerId");
                        String fotografoakBorratu = "DELETE FROM Photographers WHERE PhotographerId = " + photographerId;
                        try (Statement statement2 = DBkonexioa.getConnection().createStatement()){
                            statement2.executeUpdate(fotografoakBorratu);
                            JOptionPane.showMessageDialog(null, "ID " + photographerId + " fotografoa borratu da.");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Errore bat egon da fotografoa borratzerako orduan: " + ex);
                }
            }
        });

        //data
        this.data = new JXDatePicker();

        //interfazea
        this.goianEzkerra = new JPanel();
        goianEzkerra.add(this.photographerLabel);
        goianEzkerra.add(this.photographers);

        this.goianEskubia = new JPanel();
        goianEskubia.add(dateLabel);
        goianEskubia.add(data);

        add(this.awarded);
        add(this.remove);
        add(goianEzkerra);
        add(goianEskubia);
        add(this.lista);
        add(this.argazkia);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // argazkiak fotografoaren arabera Jlist-ean kargatzeko metodoa
    private void loadImages() {
        //aukeratutako fotografoa hartu, JXDatePicker-en data hartu eta DBkonexioa klaseko loadImages metodoa erabili, fotografoaren id-a eta aukeratutako data pasatuz, lista batean gordez.
        Photographer photographer = (Photographer) photographers.getSelectedItem();
        Date date = data.getDate();
        List<Picture> images = DBkonexioa.loadImages(photographer.getId(), date);
        //Picture objetuko modelo bat listarako
        DefaultListModel<Picture> model = new DefaultListModel<>();
        //lista errekorritu eta modeloan argazkiak sartu.
        for (Picture image : images) {
            model.addElement(image);
        }
        //modeloa Jlist-ari gehitu.
        lista.setModel(model);
    }


    public static void main(String[] args) {
        new PictureViewer();
    }
}

