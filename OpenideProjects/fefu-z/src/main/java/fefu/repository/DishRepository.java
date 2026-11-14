package fefu.repository;

import fefu.Models.Dishes;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Repository
public class DishRepository {
    private final Map<String, Dishes> dishes = new HashMap<>();

    public Dishes save(Dishes dish) {
        dishes.put(dish.id, dish);
        return dish;
    }

    public Optional<Dishes> findById(String id) {
        return Optional.ofNullable(dishes.get(id));
    }

    public List<Dishes> findAll() {
        return new ArrayList<>(dishes.values());
    }

    public void deleteById(String id)
    {
        dishes.remove(id);
    }
}