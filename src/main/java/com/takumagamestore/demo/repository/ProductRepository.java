package com.takumagamestore.demo.repository;

import com.takumagamestore.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Method sakti buat nyari list Diamond/item berdasarkan ID kategori game-nya
    List<Product> findByCategoryId(Long categoryId);
}