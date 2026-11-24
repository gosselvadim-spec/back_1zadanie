package fefu.repository;

import fefu.Models.Dishes;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dishes, String> {
    @EntityGraph(attributePaths = {"chef"})
    List<Dishes> findAll();
}