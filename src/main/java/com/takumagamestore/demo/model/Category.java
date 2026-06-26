package com.takumagamestore.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories") // Menyambungkan ke tabel 'categories' di pgAdmin
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // Contoh: "Mobile Legends"

    @Column(unique = true, nullable = false, length = 50)
    private String code; // Contoh: "mobile-legends" (buat URL slug / API)

    @Column(name = "image_url")
    private String imageUrl; // Banner atau ikon game

    @Column(name = "is_active")
    private Boolean isActive = true; // Buat nge-hide game kalau lagi maintenance
}