package fefu;

public class Dishes {
    public String id;
    public String name;
    public String status;
    public String chefId;

    public Dishes(String id, String name, String chefId) {
        this.id = id;
        this.name = name;
        this.chefId = chefId;
        this.status = "CREATED";
    }
}