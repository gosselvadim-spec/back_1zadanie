package fefu.Models;

import java.util.ArrayList;

public class Chef {
    public String id;
    public String name;
    public String type;
    public ArrayList<String> activeDishes = new ArrayList<>();

    public Chef(String id,String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }


}
