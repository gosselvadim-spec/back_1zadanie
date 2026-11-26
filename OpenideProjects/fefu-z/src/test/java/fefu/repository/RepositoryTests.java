package fefu.repository;

import fefu.Models.Chef;
import fefu.Models.Dishes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTests {

    @Autowired
    private ChefPerository chefRepository;

    @Autowired
    private DishRepository dishRepository;

    @Test
    void saveChefAndDish_ShouldSaveBothAndLinkThem() {
        Chef chef = new Chef("1", "Иван", "medium");
        chefRepository.save(chef);
        Dishes dish = new Dishes("100", "Борщ", chef);
        dishRepository.save(dish);

        Dishes savedDish = dishRepository.findById("100").orElse(null);
        assertNotNull(savedDish);
        assertEquals("Борщ", savedDish.getName());
        assertEquals("Иван", savedDish.getChef().getName());
    }
}