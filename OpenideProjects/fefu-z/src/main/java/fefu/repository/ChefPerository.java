package fefu.repository;

import fefu.Models.Chef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefPerository extends JpaRepository<Chef, String> {
}