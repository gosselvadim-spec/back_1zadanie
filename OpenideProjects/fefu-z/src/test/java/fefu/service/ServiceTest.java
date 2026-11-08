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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private ChefPerository chefRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private Service service;

    @Test
    void createChefSuccess() {
        Chef chef = new Chef("1", "Иван", "medium");
        when(chefRepository.save(any(Chef.class))).thenAnswer(i -> i.getArgument(0));
        Chef result = service.createChef("Иван", "medium");

        assertNotNull(result);
        assertEquals("Иван", result.name);
        assertEquals("medium", result.type);
    }

    @Test
    void createChef_WrongType_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.createChef("Иван", "boss");
        });
        assertEquals("Тип Повара: medium или ulti", exception.getMessage());
    }

    @Test
    void cook_DishNotFound_ThrowsException() {
        when(dishRepository.findById("123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cook("123");
        });
        assertEquals("Блюдо не найдено", exception.getMessage());
    }

    @Test
    void cook_ChefOverloaded_ThrowsException() {
        Chef chef = new Chef("1", "Иван", "medium");
        Dishes dish = new Dishes("123", "Борщ", "1");

        chef.activeDishes.add("a1");
        chef.activeDishes.add("a2");
        chef.activeDishes.add("a3");

        Dishes activeDish = new Dishes("a1", "Суп", "1");
        activeDish.status = "COOKING";

        when(dishRepository.findById("123")).thenReturn(Optional.of(dish));
        when(chefRepository.findbyId("1")).thenReturn(Optional.of(chef));
        when(dishRepository.findById("a1")).thenReturn(Optional.of(activeDish));
        when(dishRepository.findById("a2")).thenReturn(Optional.of(activeDish));
        when(dishRepository.findById("a3")).thenReturn(Optional.of(activeDish));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.cook("123");
        });
        assertEquals("Повар перегружен", exception.getMessage());
    }

    @Test
    void cook_Success() {
        Chef chef = new Chef("1", "Иван", "medium");
        Dishes dish = new Dishes("123", "Борщ", "1");

        when(dishRepository.findById("123")).thenReturn(Optional.of(dish));
        when(chefRepository.findbyId("1")).thenReturn(Optional.of(chef));

        Dishes result = service.cook("123");

        assertEquals("COOKING", result.status);
    }
}