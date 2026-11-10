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

        public Dishes cook(String dishID) {
            Dishes dish = dishs.get(dishID);
            if (dish == null) {
                throw new IllegalArgumentException("Пустое блюдо");
            }
            if (!"CREATED".equals(dish.status)){
                throw new IllegalArgumentException("блюдо готовится");
            }

            Chef chef = chefs.get(dish.chefId);
            if (chef == null) {
                throw new IllegalArgumentException("повар не найден");
            }
            int activedish = 0;
            for(String id : chef.activeDishes){
                Dishes d = dishs.get(id);
                if (d != null && "COOKING".equals(d.status)) {
                    activedish++;
                }
            }
            int limit = "ulti".equals(chef.type) ? 5 : 3;
            if (activedish >= limit) {
                throw new IllegalStateException("Повар перегружен, превышен лимит блюд");
            }

            dish.status = "COOKING";
            chef.activeDishes.add(dishID);
            return dish;
        }
    }
