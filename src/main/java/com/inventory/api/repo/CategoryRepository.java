package com.inventory.api.repo;

import com.inventory.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can add custom query methods here if needed,
    // for example: Optional<Category> findByName(String name);
}
