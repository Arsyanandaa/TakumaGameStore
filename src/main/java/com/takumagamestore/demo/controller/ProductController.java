package com.takumagamestore.demo.controller;

import com.takumagamestore.demo.model.Product;
import com.takumagamestore.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 1. GET ALL: Menampilkan semua produk item yang ada (Fitur Admin)
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. GET BY CATEGORY ID: Menampilkan produk berdasarkan Kategori Game-nya
    // Ini krusial banget dipake frontend pas user buka halaman detail game Mobile Legends
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // 3. CREATE: Menambahkan item/diamond baru (Fitur Admin Dashboard)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 4. GET BY ID: Mencari data satu item spesifik berdasarkan ID produk
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. UPDATE: Mengubah data produk/harga berdasarkan ID (Fitur Admin Dashboard)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setSku(productDetails.getSku());
                    product.setPrice(productDetails.getPrice());
                    product.setIsAvailable(productDetails.getIsAvailable());
                    product.setCategory(productDetails.getCategory()); // Bisa ubah kategori game-nya juga
                    
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok().body(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. DELETE: Menghapus produk dari etalase berdasarkan ID (Fitur Admin Dashboard)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}