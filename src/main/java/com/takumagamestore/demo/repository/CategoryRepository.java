package com.takumagamestore.demo.repository;

import com.takumagamestore.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Method tambahan buat nyari kategori berdasarkan code/slug URL-nya nanti
    Optional<Category> findByCode(String code);
}