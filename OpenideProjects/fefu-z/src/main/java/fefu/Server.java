package fefu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Server {
    private final Map<String, Chef> chefs = new HashMap<>();
    private final Map<String, Dishes> dishs = new HashMap<>();


    public Chef createChef(String name, String type){
        if(type == null || (!type.equals("medium") && !type.equals("ulti"))){
            throw new IllegalArgumentException("Тип Повара: medium или ulti");
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
            throw new IllegalArgumentException("Блюдо не найдено");
        }
        if (!"CREATED".equals(dish.status)){
            throw new IllegalArgumentException("Блюдо уже готовится или подано");
        }

        Chef chef = chefs.get(dish.chefId);
        if (chef == null) {
            throw new IllegalArgumentException("Повар не найден");
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


    public List<Chef> getChefs(String typeFilter) {
        List<Chef> result = new ArrayList<>(chefs.values());
        if (typeFilter != null) {
            result.removeIf(c -> !typeFilter.equals(c.type));
        }
        return result;
    }
    
    public List<Dishes> getDishes(String statusFilter) {
        List<Dishes> result = new ArrayList<>(dishs.values());
        if (statusFilter != null) {
            result.removeIf(d -> !statusFilter.equals(d.status));
        }
        return result;
    }

    public void updateDish(String id, String newName) {
        Dishes d = dishs.get(id);
        if (d == null) throw new IllegalArgumentException("Блюдо не найдено");
        if (newName != null) d.name = newName;
    }

    public void deleteDish(String id) {
        Dishes d = dishs.get(id);
        if (d == null) throw new IllegalArgumentException("Блюдо не найдено");
        if (!"CREATED".equals(d.status)) {
            throw new IllegalStateException("Можно удалять только блюда в статусе CREATED");
        }
        dishs.remove(id);
    }
}