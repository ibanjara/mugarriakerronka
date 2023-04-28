public class Photographer {
    private int id;
    private String name;
    private boolean awarded;

    public Photographer(int id, String name, boolean awarded) {
        this.id = id;
        this.name = name;
        this.awarded = awarded;
    }
    //geterrak
    public int getId() {
        return this.id;
    }

    // .toString() erabili ordez, zuzenean izena bueltatu
    @Override
    public String toString() {
        return this.name;
    }
}