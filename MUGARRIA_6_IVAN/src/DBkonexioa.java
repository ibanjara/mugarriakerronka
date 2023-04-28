import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBkonexioa {
    // datu basearen konfigurazioa
    private static final String USERNAME = "ivan";
    private static final String PASSWORD = "ivan";
    private static final String URL = "jdbc:mysql://localhost:3306/mugarriaivan";
    private static Connection connection;

    //datu basearen kontran konexioa egiteko
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Datu basera konektatuta.");
            } catch (SQLException e) {
                System.out.println("Errore bat egon da datu basera konektatzerako orduan: " + e);
            }
        }
        return connection;
    }

    public static List<Photographer> loadPhotographers() {
        // datu basetik fotografo guztiak aukeratu eta Arraylist bat sortzen dugu fotografoekin
        String query = "SELECT * FROM Photographers";
        List<Photographer> photographers = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            // kontsulta egin ondoren fotografoak aurkitzen diren bitartean, datuak artu eta Photographer objetu bat sortu, gero arraylist-ean sartuko duguna.
            while (resultSet.next()) {
                int id = resultSet.getInt("PhotographerId");
                String name = resultSet.getString("Name");
                boolean awarded = resultSet.getBoolean("Awarded");
                Photographer photographer = new Photographer(id, name, awarded);
                photographers.add(photographer);
            }
        } catch (SQLException e) {
            System.out.println("Zerbait gaizki joan da fotografoak eskuratzerako orduan: " + e);;
        }
        return photographers;
    }

    public static List<Picture> loadImages(int photographerId, java.util.Date date) {
        String query;
        //data aukeratuta badago JXDatePicker-en, konparatu eta data hori baino berriagoak diren argazkiak eman, bestela argazki guztiak zuzenean.
        if (date != null) {
            query = "SELECT * FROM Pictures WHERE PhotographerId = " + photographerId + " AND Date >= '" + new java.sql.Date(date.getTime()) + "'";
        } else {
            query = "SELECT * FROM Pictures WHERE PhotographerId = " + photographerId;
        }
        // argazkien arraylist bat sortuko dugu, eta argazkiak kargatuko ditugu goiko kontsultaren arabera.
        List<Picture> images = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            // datu basean argazkia aurkitzen duen bitartean, datu horiek hartu eta Picture objetu bat sortu, gero arraylist-ean sartuko duguna.
            while (resultSet.next()) {
                int id = resultSet.getInt("PictureId");
                String title = resultSet.getString("Title");
                Date dateValue = resultSet.getDate("Date");
                String file = resultSet.getString("File");
                int visits = resultSet.getInt("Visits");
                Picture picture = new Picture(id, title, dateValue, file, visits, photographerId);
                images.add(picture);
            }
        } catch (SQLException e) {
            System.out.println("Errore bat egon da argazkiak kargatzerako orduan" + e);
        }
        return images;
    }

    // datu basean bisitak eguneratzeko.
    public static void updateVisits(Picture picture) {
        String query = "UPDATE Pictures SET Visits = " + picture.getVisits() + " WHERE PictureId = " + picture.getId();
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Errore bat egon da bisitak eguneratzerako orduan: " + e);;
        }
    }
}
