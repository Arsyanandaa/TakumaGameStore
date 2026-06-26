package com.takumagamestore.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke tabel users (siapa yang beli)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relasi ke tabel products (item apa yang dibeli)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "target_id", nullable = false, length = 50)
    private String targetId;

    @Column(name = "zone_id", length = 20)
    private String zoneId; // Zone ID Game (contoh: 2123, biasanya buat ML)

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice; // Total harga yang harus dibayar

    @Column(nullable = false, length = 50)
    private String status = "PENDING"; // PENDING, SUCCESS, FAILED

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // Contoh: "QRIS", "BCA_VA" (sementara pake String dulu)

    @Column(name = "invoice_number", unique = true, nullable = false, length = 50)
    private String invoiceNumber; // Kode invoice unik (Contoh: TKM-20260625-001)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "qr_code_url", length = 500)
    private String qrCodeUrl; //

    @Column(name = "order_id")
    private String orderId;
}