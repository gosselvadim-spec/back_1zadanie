package fefu.repository;

import fefu.Models.Chef;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Repository
public class ChefPerository{
    private final Map<String, Chef> chefs = new HashMap<>();

    public Chef save(Chef chef){
        chefs.put(chef.Id,chef);
        return chef;
    }
    public Optional<Chef> findbyId(String Id){
        return Optional.ofNullable(chefs.get(Id));
    }
    public List<Chef> findAll() {
        return new ArrayList<>(chefs.values());
    }
    public void deletebyId(String id) {
        chefs.remove(id);
    }
}