package com.takumagamestore.demo.dto;

import lombok.Data;

@Data // Tetep pake magic Lombok biar gak ngetik getter setter manual
public class TransactionDTO {
    private Long userId;          // ID user yang beli
    private Long productId;       // ID diamond/item yang dipilih
    private String targetId;      // ID Game (misal: 84739281)
    private String zoneId;        // Zone ID (misal: 2123, opsional buat game tertentu)
    private String paymentMethod; // Kode pembayaran (misal: "QRIS", "BCA_VA")
}