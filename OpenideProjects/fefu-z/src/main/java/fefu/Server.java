package fefu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server {
        private final Map<String, Chef> chefs = new HashMap<>();
        private final Map<String, Dishes> dishs = new HashMap<>();


        public Chef createChef(String name, String type){
            if(type == null || (!type.equals("medium") && !type.equals("ulti"))){
                throw new IllegalArgumentException("Тип Поавара: medium or hard");
            }
            String id = UUID.randomUUID().toString();

            Chef chef = new Chef(id,name,type);
            chefs.put(id,chef);

            return chef;
        }

        public Dishes creatwdishes(String name, String idchef){
            String id = UUID.randomUUID().toString();

            Dishes dish = new Dishes(id,name,idchef);
            dishs.put(id,dish);

            return dish;
        }
    }
