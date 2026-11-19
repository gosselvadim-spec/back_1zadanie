package fefu.Models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chefs")
public class Chef {
    @Id
    private String id;
    private String name;
    private String type;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dishes> activeDishes = new ArrayList<>();

    public Chef() {}

    public Chef(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<Dishes> getActiveDishes() { return activeDishes; }
    public void setActiveDishes(List<Dishes> activeDishes) { this.activeDishes = activeDishes; }
}