package fefu.Controler;

import fefu.Models.Chef;
import fefu.service.KitchenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chefs")
public class ChefControler {

    private final KitchenService kitchenService;

    public ChefControler(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @PostMapping
    public ResponseEntity<Chef> createChef(@RequestBody Map<String, String> body) {
        Chef chef = kitchenService.createChef(body.get("name"), body.get("type"));
        return ResponseEntity.status(HttpStatus.CREATED).body(chef);
    }

    @GetMapping
    public List<Chef> getChefs(@RequestParam(required = false) String type) {
        return kitchenService.getChefs(type);
    }

    @GetMapping("/{id}")
    public Chef getChefById(@PathVariable String id) {
        return kitchenService.getChefById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChef(@PathVariable String id) {
        kitchenService.deleteChef(id);
        return ResponseEntity.noContent().build();
    }
}