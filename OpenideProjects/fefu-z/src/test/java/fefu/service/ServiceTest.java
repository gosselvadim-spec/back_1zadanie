package fefu.service;

import fefu.Models.Chef;
import fefu.Models.Dishes;
import fefu.repository.ChefPerository;
import fefu.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private ChefPerository chefRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private KitchenService kitchenService;

    @Test
    void createChef_Success() {
        Chef chef = new Chef("1", "Иван", "medium");
        when(chefRepository.save(any(Chef.class))).thenAnswer(i -> i.getArgument(0));

        Chef result = kitchenService.createChef("Иван", "medium");

        assertNotNull(result);
        assertEquals("Иван", result.getName());
    }

    @Test
    void createChef_WrongType_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            kitchenService.createChef("Иван", "boss");
        });
    }

    @Test
    void cook_ChefOverloaded_ThrowsException() {
        Chef chef = new Chef("1", "Иван", "medium");
        Dishes dish = new Dishes("123", "Борщ", chef);
        Dishes d1 = new Dishes("a1", "Суп", chef); d1.setStatus("COOKING");
        Dishes d2 = new Dishes("a2", "Салат", chef); d2.setStatus("COOKING");
        Dishes d3 = new Dishes("a3", "Каша", chef); d3.setStatus("COOKING");
        chef.getActiveDishes().add(d1);
        chef.getActiveDishes().add(d2);
        chef.getActiveDishes().add(d3);

        when(dishRepository.findById("123")).thenReturn(Optional.of(dish));

        assertThrows(IllegalStateException.class, () -> {
            kitchenService.cook("123");
        });
    }

    @Test
    void transferDish_Success() {
        Chef oldChef = new Chef("1", "Иван", "medium");
        Chef newChef = new Chef("2", "Петр", "ulti");
        Dishes dish = new Dishes("123", "Борщ", oldChef);

        when(dishRepository.findById("123")).thenReturn(Optional.of(dish));
        when(chefRepository.findById("2")).thenReturn(Optional.of(newChef));
        when(dishRepository.save(any(Dishes.class))).thenAnswer(i -> i.getArgument(0));

        Dishes result = kitchenService.transferDishToAnotherChef("123", "2");

        assertEquals(newChef, result.getChef());
    }
}