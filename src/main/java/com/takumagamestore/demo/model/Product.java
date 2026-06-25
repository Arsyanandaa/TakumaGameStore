package com.takumagamestore.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi Foreign Key ke tabel categories
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String name; // Contoh: "86 Diamonds"

    @Column(unique = true, nullable = false, length = 50)
    private String sku; // Kode unik untuk API supplier (Contoh: "ML86")

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price; // Harga item (Wajib pakai BigDecimal biar presisi hitung duit)

    @Column(name = "is_available")
    private Boolean isAvailable = true; // Buat nge-hide produk kalau stok abis
}