package fefu.Models;

public class Dishes {
    public String Id;
    public String name;
    public String status;
    public String chefId;

    public Dishes(String id, String name, String chefId) {
        this.Id = Id;
        this.name = name;
        this.chefId = chefId;
        this.status = "CREATED";
    }
}