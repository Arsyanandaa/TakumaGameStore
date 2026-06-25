package com.takumagamestore.demo.controller;

import com.takumagamestore.demo.model.Category;
import com.takumagamestore.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. GET ALL: Menampilkan semua kategori game (Buat dipajang di Homepage)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 2. CREATE: Menambahkan kategori game baru (Fitur Admin Dashboard)
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // 3. GET BY ID: Mencari satu kategori game berdasarkan ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. GET BY CODE/SLUG: Mencari game berdasarkan code (Contoh: /api/categories/slug/mobile-legends)
    // Ini berguna banget pas user klik game di homepage, terus di-direct ke halaman detail game tersebut
    @GetMapping("/slug/{code}")
    public ResponseEntity<Category> getCategoryByCode(@PathVariable String code) {
        return categoryRepository.findByCode(code)
                .map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. UPDATE: Mengubah data kategori game berdasarkan ID (Fitur Admin Dashboard)
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDetails.getName());
                    category.setCode(categoryDetails.getCode());
                    category.setImageUrl(categoryDetails.getImageUrl());
                    category.setIsActive(categoryDetails.getIsActive()); // Bisa set true/false di sini
                    
                    Category updatedCategory = categoryRepository.save(category);
                    return ResponseEntity.ok().body(updatedCategory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. DELETE: Menghapus kategori game berdasarkan ID (Fitur Admin Dashboard)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}