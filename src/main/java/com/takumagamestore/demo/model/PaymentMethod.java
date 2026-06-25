package com.takumagamestore.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // Contoh: "BCA Virtual Account", "QRIS"

    @Column(unique = true, nullable = false, length = 50)
    private String code; // Contoh: "bca_va", "qris" (buat API identifier)

    @Column(length = 50)
    private String type; // Contoh: "E-WALLET", "VIRTUAL_ACCOUNT"

    @Column(name = "image_url")
    private String imageUrl; // Logo bank atau e-wallet-nya

    @Column(name = "is_active")
    private Boolean isActive = true; // Buat matiin metode kalau bank pas offline
}