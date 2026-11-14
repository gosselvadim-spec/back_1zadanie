package fefu.Controler;

import fefu.Models.Dishes;
import fefu.service.KitchenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/dishes")

public class DishController {
    private final KitchenService kitchenService;

    public DishController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }
    @PostMapping
    public ResponseEntity<Dishes> createDish(@RequestBody Map<String,String> body){
        Dishes dish = kitchenService.createDish(body.get("name"),body.get("chefId"));
        return ResponseEntity.status(HttpStatus.CREATED).body(dish);
    }

    @PostMapping("/{id}/cook")
    public Dishes cookDish(@PathVariable String id) {
        return kitchenService.cook(id);
    }

    @GetMapping
    public List<Dishes> getDishes(@RequestParam(required = false) String status) {
        return kitchenService.getDishes(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateDish(@PathVariable String id, @RequestBody Map<String, String> body) {
        kitchenService.updateDish(id, body.get("name"));
        return ResponseEntity.ok(Map.of("message", "Блюдо обновлено"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable String id) {
        kitchenService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

}
