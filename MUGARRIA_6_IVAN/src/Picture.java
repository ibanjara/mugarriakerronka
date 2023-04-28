import java.util.Date;

public class Picture {
    private int id;
    private String title;
    private Date date;
    private String file;
    private int visits;
    private int photographerId;

    public Picture(int id, String title, Date date, String file, int visits, int photographerId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.file = file;
        this.visits = visits;
        this.photographerId = photographerId;
    }

    //geterrak
    public int getId() {
        return this.id;
    }

    public String getFile() {
        return this.file;
    }

    public int getVisits() {
        return this.visits;
    }

    //incrementVisits metodoa argazki bat ikusterakoan +1 bisita datu basean gehitzeko.
    public void incrementVisits() {
        visits++;
        DBkonexioa.updateVisits(this);
    }
    // .toString() erabili ordez, zuzenean titulua bueltatu
    @Override
    public String toString() {
        return this.title;
    }
}
