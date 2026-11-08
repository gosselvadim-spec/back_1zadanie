package fefu.service;

import fefu.Models.Chef;
import fefu.Models.Dishes;
import fefu.repository.ChefPerository;
import fefu.repository.DishRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class Service {

    private final ChefPerository chefRepository;
    private final DishRepository dishRepository;

    public Service(ChefPerository chefRepository, DishRepository dishRepository) {
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
        if (chefRepository.findbyId(chefId).isEmpty()) {
            throw new IllegalArgumentException("Повар не найден");
        }
        Dishes dish = new Dishes(UUID.randomUUID().toString(), name, chefId);
        return dishRepository.save(dish);
    }

    public Dishes cook(String dishId) {
        Dishes dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));

        if (!"CREATED".equals(dish.status)) {
            throw new IllegalArgumentException("Блюдо уже готовится или подано");
        }

        Chef chef = chefRepository.findbyId(dish.chefId).orElseThrow(() -> new IllegalArgumentException("Повар не найден"));

        int activeCount = 0;
        for (String id : chef.activeDishes) {
            Optional<Dishes> d = dishRepository.findById(id);
            if (d.isPresent() && "COOKING".equals(d.get().status)) {
                activeCount++;
            }
        }

        int limit = "ulti".equals(chef.type) ? 10 : 3;
        if (activeCount >= limit) {
            throw new IllegalStateException("Повар перегружен");
        }

        dish.status = "COOKING";
        chef.activeDishes.add(dishId);
        dishRepository.save(dish);
        return dish;
    }

    public List<Chef> getChefs(String typeFilter) {
        List<Chef> result = chefRepository.findAll();
        if (typeFilter != null) {
            result.removeIf(c -> !typeFilter.equals(c.type));
        }
        return result;
    }

    public List<Dishes> getDishes(String statusFilter) {
        List<Dishes> result = dishRepository.findAll();
        if (statusFilter != null) {
            result.removeIf(d -> !statusFilter.equals(d.status));
        }
        return result;
    }

    public void updateDish(String id, String newName) {
        Dishes dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));
        if (newName != null) {
            dish.name = newName;
            dishRepository.save(dish);
        }
    }

    public void deleteDish(String id) {
        Dishes dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено"));
        if (!"CREATED".equals(dish.status)) {
            throw new IllegalStateException("Можно удалять только блюда в статусе CREATED");
        }
        dishRepository.deletebyId(id);
    }
}