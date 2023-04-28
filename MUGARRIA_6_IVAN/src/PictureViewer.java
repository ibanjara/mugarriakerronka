import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

//DATARENA FROGATZEKO 04/28/2023 AUKERATU
public class PictureViewer extends JFrame {
    private JPanel goianEzkerra, goianEskubia;
    private JLabel photographerLabel, dateLabel, argazkia;
    private JComboBox photographers;
    private JXDatePicker data;
    private JList<Picture> lista;

    public PictureViewer(){
        setTitle("Mugarria 6 - Ivan Jara");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000,600));

        //gridlayout
        this.setLayout(new GridLayout(2,2,20,20));

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

        //Listako argazkian behin klik egiterakoan argazki hori erakutsi eta +1 bisita gehitu argazki horri Horretarako MouseListener-a.
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Picture selectedImage = lista.getSelectedValue();
                    ImageIcon imageIcon = new ImageIcon(selectedImage.getFile());
                    Image image = imageIcon.getImage().getScaledInstance(250,250,Image.SCALE_SMOOTH);
                    ImageIcon scaledImageIcon = new ImageIcon(image);
                    argazkia.setIcon(scaledImageIcon);
                    selectedImage.incrementVisits();
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

