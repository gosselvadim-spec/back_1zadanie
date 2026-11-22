package fefu.service;

import fefu.Models.Chef;
import fefu.Models.Dishes;
import fefu.repository.ChefPerository;
import fefu.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KitchenService {

    private final ChefPerository chefRepository;
    private final DishRepository dishRepository;

    public KitchenService(ChefPerository chefRepository, DishRepository dishRepository) {
        this.chefRepository = chefRepository;
        this.dishRepository = dishRepository;
    }

    public Chef createChef(String name, String type) {
        if (type == null || (!type.equals("medium") && !type.equals("ulti"))) {
            throw new IllegalArgumentException("Тип Повара: medium или ulti");
        }
        Chef chef = new Chef(UUID.randomUUID().toString(), name, type);
        return chefRepository.save(chef);
    }

    public Dishes createDish(String name, String chefId) {
        Chef chef = chefRepository.findById(chefId)
                .orElseThrow(() -> new IllegalArgumentException("Повар не найден"));

        Dishes dish = new Dishes(UUID.randomUUID().toString(), name, chef);
        return dishRepository.save(dish);
    }

    @Transactional
    public Dishes cook(String dishId) {
        Dishes dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));

        if (!"CREATED".equals(dish.getStatus())) {
            throw new IllegalArgumentException("Блюдо уже готовится или подано");
        }

        Chef chef = dish.getChef();

        int activeCount = 0;
        for (Dishes d : chef.getActiveDishes()) {
            if ("COOKING".equals(d.getStatus())) {
                activeCount++;
            }
        }

        int limit = "ulti".equals(chef.getType()) ? 10 : 3;
        if (activeCount >= limit) {
            throw new IllegalStateException("Повар перегружен");
        }

        dish.setStatus("COOKING");
        dishRepository.save(dish);
        return dish;
    }

    public List<Chef> getChefs(String typeFilter) {
        List<Chef> result = chefRepository.findAll();
        if (typeFilter != null) {
            result.removeIf(c -> !typeFilter.equals(c.getType()));
        }
        return result;
    }

    public List<Dishes> getDishes(String statusFilter) {
        List<Dishes> result = dishRepository.findAll();
        if (statusFilter != null) {
            result.removeIf(d -> !statusFilter.equals(d.getStatus()));
        }
        return result;
    }

    public void updateDish(String id, String newName) {
        Dishes dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));
        if (newName != null) {
            dish.setName(newName);
            dishRepository.save(dish);
        }
    }

    public void deleteDish(String id) {
        Dishes dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));
        if (!"CREATED".equals(dish.getStatus())) {
            throw new IllegalStateException("Можно удалить только блюда в статусе CREATED");
        }
        dishRepository.deleteById(id);
    }

    public Dishes getDishById(String id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));
    }

    public Chef getChefById(String id) {
        return chefRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Повар не найден"));
    }

    public void deleteChef(String id) {
        Chef chef = chefRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Повар не найден"));

        if (!chef.getActiveDishes().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить повара, у него есть блюда");
        }

        chefRepository.deleteById(id);
    }
}