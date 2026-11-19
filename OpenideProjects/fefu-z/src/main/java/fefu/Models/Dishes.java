package fefu.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "dishes")
public class Dishes {
    @Id
    private String id;
    private String name;
    private String status;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private Chef chef;

    public Dishes() {}

    public Dishes(String id, String name, Chef chef) {
        this.id = id;
        this.name = name;
        this.chef = chef;
        this.status = "CREATED";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Chef getChef() { return chef; }
    public void setChef(Chef chef) { this.chef = chef; }
}